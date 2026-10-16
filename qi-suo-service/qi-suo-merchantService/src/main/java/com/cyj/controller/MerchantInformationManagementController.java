package com.cyj.controller;


import com.cyj.Annotation.Log;
import com.cyj.common.Result;
import com.cyj.dto.UpdateMerchantInformationFormDTO;
import com.cyj.dvo.MerchantInformationDVO;
import com.cyj.service.MerchantInformationManagementService;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

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
}
