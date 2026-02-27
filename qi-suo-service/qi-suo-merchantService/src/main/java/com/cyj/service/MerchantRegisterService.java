package com.cyj.service;

import com.cyj.common.Result;
import com.cyj.dto.MerchantRegisterDTO;

public interface MerchantRegisterService {
    Result<String> register(MerchantRegisterDTO merchantRegisterDTO);
}
