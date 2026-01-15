package com.cyj.service;

import com.cyj.common.Result;
import com.cyj.dto.UserLoginDTO;

public interface UserService {

    Result<String> userLogin(UserLoginDTO userLoginDTO);
}
