package com.cyj.service.userService;

import com.cyj.common.Result;
import com.cyj.dto.UserLoginDTO;
import com.cyj.dvo.AccessTokenDVO;

public interface UserService {

    Result<AccessTokenDVO> userLogin(UserLoginDTO userLoginDTO);
}
