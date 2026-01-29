package com.cyj.controller;


import com.cyj.common.Result;
import com.cyj.dto.NewAdminDTO;
import com.cyj.dvo.AdminDVO;
import com.cyj.service.AdminManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 管理员管理
 */
@RestController
@RequestMapping("/api/admin/admin")
public class adminManagementController {
    @Autowired
    private AdminManagementService adminManagementService;
    /**
     *管理员列表查询
     */
    @GetMapping("/getAdminList")
    public Result<List<AdminDVO>> getAdminList(){
        return adminManagementService.getAdminList();
    }

    /**
     *新增管理员
     */
    @PostMapping("/newAdmin")
    public Result<AdminDVO> newAdmin(@RequestBody NewAdminDTO newAdminDTO){
        return adminManagementService.newAdmin(newAdminDTO);
    }

    /**
     *启用管理员
     */

    /**
     * 禁用管理员
     */

    /**
     * 编辑管理员(修改权限、信息)
     */

    /**
     * 删除管理员
     */
}
