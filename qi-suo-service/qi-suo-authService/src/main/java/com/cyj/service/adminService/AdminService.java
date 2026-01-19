package com.cyj.service.adminService;

import com.cyj.common.Result;
import com.cyj.dto.AdminLoginDTO;
import com.cyj.dto.UserLoginDTO;

public interface AdminService {

    Result<String> adminLogin(AdminLoginDTO adminLoginDTO);
}
