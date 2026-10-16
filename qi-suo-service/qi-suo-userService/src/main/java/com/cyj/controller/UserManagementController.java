package com.cyj.controller;

import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dto.AdminUserListSelectConditionDTO;
import com.cyj.dvo.AdminUserManagementLDVO;
import com.cyj.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 用户信息管理：用户表由用户服务提供，管理服务的用户管理页面经服务间调用走这里
 */
@RestController
@RequestMapping("/api/user/userManagement")
public class UserManagementController {
    @Autowired
    private UserManagementService userManagementService;

    /**
     * 获取用户列表
     */
    @PreAuthorize("hasAnyAuthority('SystemADMIN','SuperAdmin','system')")
    @PostMapping("/list")
    public Result<PageResult<AdminUserManagementLDVO>> list(@RequestBody AdminUserListSelectConditionDTO adminUserListSelectConditionDTO) {

        return userManagementService.list(adminUserListSelectConditionDTO);
    }

    /**
     * 用户账号状态设置
     */
    @PreAuthorize("hasAnyAuthority('SystemADMIN','SuperAdmin','system')")
    @PutMapping("/status")
    public Result<String> status(@RequestParam Long id, @RequestParam Integer pageNum) {
        return userManagementService.setStatus(id,pageNum);
    }
}
