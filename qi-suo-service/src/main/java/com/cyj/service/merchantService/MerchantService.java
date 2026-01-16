package com.cyj.service.merchantService;

import com.cyj.common.Result;
import com.cyj.dto.MerchantLoginDTO;
import com.cyj.dto.UserLoginDTO;

public interface MerchantService {

    Result<String> merchantLogin(MerchantLoginDTO merchantLoginDTO);
}
