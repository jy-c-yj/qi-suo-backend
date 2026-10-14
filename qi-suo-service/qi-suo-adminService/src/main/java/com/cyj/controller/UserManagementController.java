package com.cyj.controller;

import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dto.AdminUserListSelectConditionDTO;
import com.cyj.dvo.AdminUserManagementLDVO;
import com.cyj.service.AdminUserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制器类
 * 该类用于处理与用户管理相关的请求和操作
 */
@RestController
@RequestMapping("/api/admin/userManagement")
public class UserManagementController {
    @Autowired
    private AdminUserManagementService userManagementService;

    /**
     * 获取用户列表
     * @param adminUserListSelectConditionDTO
     * @return
     */
    @PostMapping("/list")
    public Result<PageResult<AdminUserManagementLDVO>> list(@RequestBody AdminUserListSelectConditionDTO adminUserListSelectConditionDTO) {

        return userManagementService.list(adminUserListSelectConditionDTO);
    }
    /**
     * 用户账号状态设置
     */
    @PutMapping("/status")
    public Result<String> status(@RequestParam Long id, @RequestParam Integer pageNum) {
        return userManagementService.status(id,pageNum);
    }
}
