package com.cyj.service;

import com.cyj.common.Result;
import com.cyj.dto.ChangePasswordDTO;
import com.cyj.dto.UpdateMerchantInformationFormDTO;
import com.cyj.dvo.MerchantInformationDVO;
import org.springframework.web.multipart.MultipartFile;

public interface MerchantInformationManagementService {

    public Result<MerchantInformationDVO> getMerchantInformation();

    Result<String> updateMerchantInformation(UpdateMerchantInformationFormDTO updateMerchantInformationFormDTO);

    /**
     * 上传店铺 logo：调用文件服务保存文件并立即写入商家表
     */
    Result<String> uploadLogo(MultipartFile file);

    /**
     * 修改登录密码
     */
    Result<String> changePassword(ChangePasswordDTO changePasswordDTO);
}
