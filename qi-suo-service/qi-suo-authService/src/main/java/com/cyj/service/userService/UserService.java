package com.cyj.service.userService;

import com.cyj.common.Result;
import com.cyj.dto.UserLoginDTO;

public interface UserService {

    Result<String> userLogin(UserLoginDTO userLoginDTO);
}
