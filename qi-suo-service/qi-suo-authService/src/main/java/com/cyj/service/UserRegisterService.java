package com.cyj.service;

import com.cyj.common.Result;
import com.cyj.dto.UserRegisterDTO;

public interface UserRegisterService {
    Result<String> register(UserRegisterDTO userRegisterDTO);
}
