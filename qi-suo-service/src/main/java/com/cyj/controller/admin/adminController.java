package com.cyj.controller.admin;


import com.cyj.common.Result;
import com.cyj.dto.AdminLoginDTO;
import com.cyj.dto.UserLoginDTO;
import com.cyj.service.adminService.AdminService;
import com.cyj.service.userService.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/api/admin")
public class adminController {
    @Autowired
    AdminService adminService;
    @Autowired
    HttpServletRequest request;
    @PostMapping("/login")
    public Result<String> login(@RequestBody AdminLoginDTO adminLoginDTO) {

        return adminService.adminLogin(adminLoginDTO);

    }
}
