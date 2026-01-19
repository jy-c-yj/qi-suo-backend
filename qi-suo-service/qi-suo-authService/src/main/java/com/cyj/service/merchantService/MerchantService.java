package com.cyj.service.merchantService;

import com.cyj.common.Result;
import com.cyj.dto.MerchantLoginDTO;
import com.cyj.dto.UserLoginDTO;
import com.cyj.dvo.AccessTokenDVO;

public interface MerchantService {

    Result<AccessTokenDVO> merchantLogin(MerchantLoginDTO merchantLoginDTO);
}
