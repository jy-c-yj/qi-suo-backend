package com.cyj.service;

import com.cyj.common.Result;
import com.cyj.dto.UpdateMerchantInformationFormDTO;
import com.cyj.dvo.MerchantInformationDVO;

public interface MerchantInformationManagement {

    public Result<MerchantInformationDVO> getMerchantInformation();

    Result<String> updateMerchantInformation(UpdateMerchantInformationFormDTO updateMerchantInformationFormDTO);
}
