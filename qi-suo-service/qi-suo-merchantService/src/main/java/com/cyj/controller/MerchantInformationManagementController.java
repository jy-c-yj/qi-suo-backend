package com.cyj.controller;


import com.cyj.Annotation.Log;
import com.cyj.common.Result;
import com.cyj.dto.ChangePasswordDTO;
import com.cyj.dto.UpdateMerchantInformationFormDTO;
import com.cyj.dvo.MerchantInformationDVO;
import com.cyj.service.MerchantInformationManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/merchant/Information/")
public class MerchantInformationManagementController {

    @Autowired
    private MerchantInformationManagementService merchantInformationManagement;
    /**
     * 查询信息
     */
    @PreAuthorize("hasAuthority('Merchant')")
    @GetMapping("/get")
    public Result<MerchantInformationDVO> getMerchantInformation() {
            return merchantInformationManagement.getMerchantInformation();
    }

    /**
     * 修改信息
     */
    @Log(module = "店铺信息", desc = "修改店铺信息")
    @PreAuthorize("hasAuthority('Merchant')")
    @PostMapping("/update")
    public Result<String> updateMerchantInformation(@RequestBody UpdateMerchantInformationFormDTO updateMerchantInformationFormDTO) {
        return merchantInformationManagement.updateMerchantInformation(updateMerchantInformationFormDTO);
    }

    /**
     * 上传店铺 logo（上传后立即写入数据库）
     */
    @Log(module = "店铺信息", desc = "上传店铺Logo")
    @PreAuthorize("hasAuthority('Merchant')")
    @PostMapping("/uploadLogo")
    public Result<String> uploadLogo(@RequestParam MultipartFile file) {
        return merchantInformationManagement.uploadLogo(file);
    }

    /**
     * 修改商家登录密码
     */
    @Log(module = "店铺信息", desc = "修改商家密码")
    @PreAuthorize("hasAuthority('Merchant')")
    @PostMapping("/changePassword")
    public Result<String> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        return merchantInformationManagement.changePassword(changePasswordDTO);
    }
}
