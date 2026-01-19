package com.cyj.service.adminService;

import com.cyj.common.Result;
import com.cyj.dto.AdminLoginDTO;
import com.cyj.dto.UserLoginDTO;
import com.cyj.dvo.AccessTokenDVO;

public interface AdminService {

    Result<AccessTokenDVO> adminLogin(AdminLoginDTO adminLoginDTO);
}
