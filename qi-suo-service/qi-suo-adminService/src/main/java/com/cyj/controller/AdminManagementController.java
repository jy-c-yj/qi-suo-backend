package com.cyj.controller;


import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dto.AdminDTO;
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
@RequestMapping("/api/admin/adminManagement")
public class AdminManagementController {
    @Autowired
    private AdminManagementService adminManagementService;
    /**
     *管理员列表查询
     */
    @GetMapping("/getAdminList")
    public Result<PageResult<AdminDVO>> getAdminList(int pageNum,Long pageSize){
        return adminManagementService.getAdminList(pageNum,pageSize);
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
    @PutMapping("/setAdminIsEnabled")
    public Result<Boolean> setAdminIsEnabled(@RequestParam Long adminId,@RequestParam Integer pageNum){
        return adminManagementService.setAdminIsEnabled(adminId,pageNum);
    }
    /**
     * 禁用管理员
     */
    @PutMapping("/setAdminNotEnabled")
    public Result<Boolean> setAdminNotEnabled(@RequestParam Long adminId,@RequestParam Integer pageNum){
        return adminManagementService.setAdminNotEnabled(adminId,pageNum);
    }

    /**
     * 编辑管理员(修改权限、信息)
     */
    @PutMapping("/updateAdmin")
    public Result<Boolean> updateAdmin(@RequestBody AdminDTO adminDTO,@RequestParam Integer pageNum){
        return adminManagementService.updateAdmin(adminDTO,pageNum);
    }

    /**
     * 删除管理员
     */
    @DeleteMapping("/deleteAdmin/{adminId}")
    public Result<Boolean> deleteAdmin(@PathVariable Long adminId){
        return adminManagementService.deleteAdmin(adminId);
    }
}
