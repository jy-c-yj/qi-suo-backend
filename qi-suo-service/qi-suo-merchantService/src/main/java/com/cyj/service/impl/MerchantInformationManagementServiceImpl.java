package com.cyj.service.impl;

import com.cyj.common.Result;
import com.cyj.dto.ChangePasswordDTO;
import com.cyj.dto.UpdateMerchantInformationFormDTO;
import com.cyj.dvo.MerchantInformationDVO;
import com.cyj.entity.Merchant;
import com.cyj.feign.file.FileFeign;
import com.cyj.mapper.MerchantMapper;
import com.cyj.service.MerchantInformationManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
@Slf4j
public class MerchantInformationManagementServiceImpl implements MerchantInformationManagementService {
    @Autowired
    private MerchantMapper merchantMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private FileFeign fileFeign;
    @Override
    public Result<MerchantInformationDVO> getMerchantInformation() {
        //获取商家id
        Long merchantId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("商家id --> {}", merchantId);
        //获取信息
        Merchant merchant = null;
        try {
            merchant = merchantMapper.selectById(merchantId);

        }catch (Exception e){
            log.error("获取商家信息出错 ， merchantId --》 {} ， 错误信息 -- 》\n {}",merchantId,e.getMessage());
            return Result.error("获取商家信息出错");
        }
        if(merchant == null){
            return Result.error("获取商家信息异常，数据为空");
        }
        //返回数据
        MerchantInformationDVO merchantInformationDVO = MerchantInformationDVO.builder()
                .merchantId(merchantId)
                .merchantName(merchant.getMerchantName())
                .shopName(merchant.getShopName())
                .shopLogoUrl(merchant.getShopLogoUrl())
                .description(merchant.getDescription())
                .contactEmail(merchant.getContactEmail())
                .contactName(merchant.getContactName())
                .contactPhone(merchant.getContactPhone())
                .status(merchant.getStatus())
                .createTime(merchant.getCreateTime())
                .updateTime(merchant.getUpdateTime())
                .updateBy(merchant.getUpdateBy())
                .build();

        return Result.success(merchantInformationDVO);
    }

    @Override
    public Result<String> updateMerchantInformation(UpdateMerchantInformationFormDTO updateMerchantInformationFormDTO) {

        if(updateMerchantInformationFormDTO == null){
            return Result.error("修改失败，数据不完整");
        }

        //修改数据库
        try {
            merchantMapper.updateMerchantInformation(updateMerchantInformationFormDTO);
        }catch (Exception e){
            log.error("修改失败，数据库操作失败 , 错误信息 --》 {}",e.getMessage());
            return Result.error("修改失败，数据库操作失败");
        }


        return Result.success("修改成功");
    }

    @Override
    public Result<String> uploadLogo(MultipartFile file) {
        Long merchantId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (file == null || file.isEmpty()) {
            return Result.error("请选择要上传的图片");
        }
        Result<String> uploadResult = fileFeign.upload(file);
        if (uploadResult == null || uploadResult.getCode() == 0 || uploadResult.getData() == null) {
            return Result.error(uploadResult == null ? "图片上传失败" : uploadResult.getMsg());
        }
        String url = uploadResult.getData();

        UpdateMerchantInformationFormDTO updateDTO = new UpdateMerchantInformationFormDTO();
        updateDTO.setMerchantId(merchantId);
        updateDTO.setShopLogoUrl(url);
        merchantMapper.updateMerchantInformation(updateDTO);

        // URL 必须放进 data（Result.success(String) 会写进 msg）
        Result<String> result = Result.success();
        result.setData(url);
        result.setMsg("上传成功");
        return result;
    }

    @Override
    public Result<String> changePassword(ChangePasswordDTO changePasswordDTO) {
        Long merchantId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (changePasswordDTO == null
                || changePasswordDTO.getOldPassword() == null || changePasswordDTO.getOldPassword().isBlank()
                || changePasswordDTO.getNewPassword() == null || changePasswordDTO.getNewPassword().isBlank()) {
            return Result.error("请输入旧密码和新密码");
        }
        Merchant merchant = merchantMapper.selectById(merchantId);
        if (merchant == null) {
            return Result.error("商家不存在");
        }
        if (!matchesPassword(changePasswordDTO.getOldPassword(), merchant.getMerchantPassword())) {
            return Result.error("旧密码不正确");
        }

        UpdateMerchantInformationFormDTO updateDTO = new UpdateMerchantInformationFormDTO();
        updateDTO.setMerchantId(merchantId);
        updateDTO.setMerchantPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        merchantMapper.updateMerchantInformation(updateDTO);
        return Result.success("密码修改成功");
    }

    /**
     * 兼容有 {id} 前缀的加密密码与历史明文密码
     */
    private boolean matchesPassword(String rawPassword, String storedPassword) {
        if (storedPassword == null) {
            return false;
        }
        try {
            return passwordEncoder.matches(rawPassword, storedPassword);
        } catch (IllegalArgumentException e) {
            return storedPassword.equals(rawPassword);
        }
    }
}
