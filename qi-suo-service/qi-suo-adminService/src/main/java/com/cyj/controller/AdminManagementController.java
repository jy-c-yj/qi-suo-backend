package com.cyj.controller;


import com.cyj.Annotation.Log;
import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dto.AdminDTO;
import com.cyj.dto.NewAdminDTO;
import com.cyj.dvo.AdminDVO;
import com.cyj.service.AdminManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 管理员管理
 * 系统管理员可做任何操作；超级/普通管理员按层级规则操作；'system' 是服务间调用注入的固定身份
 */
@RestController
@RequestMapping("/api/admin/adminManagement")
public class AdminManagementController {
    @Autowired
    private AdminManagementService adminManagementService;
    /**
     *管理员列表查询
     */
    @PreAuthorize("hasAnyAuthority('SystemADMIN','SuperAdmin','OrdinaryAdmin','system')")
    @GetMapping("/getAdminList")
    public Result<PageResult<AdminDVO>> getAdminList(int pageNum,Long pageSize){
        return adminManagementService.getAdminList(pageNum,pageSize);
    }

    /**
     *新增管理员
     */
    @Log(module = "管理员", desc = "新增管理员")
    @PreAuthorize("hasAnyAuthority('SystemADMIN','SuperAdmin','system')")
    @PostMapping("/newAdmin")
    public Result<AdminDVO> newAdmin(@RequestBody NewAdminDTO newAdminDTO){
        return adminManagementService.newAdmin(newAdminDTO);
    }

    /**
     *启用管理员
     */
    @Log(module = "管理员", desc = "启用管理员")
    @PreAuthorize("hasAnyAuthority('SystemADMIN','SuperAdmin','OrdinaryAdmin','system')")
    @PutMapping("/setAdminIsEnabled")
    public Result<Boolean> setAdminIsEnabled(@RequestParam Long adminId,@RequestParam Integer pageNum){
        return adminManagementService.setAdminIsEnabled(adminId,pageNum);
    }
    /**
     * 禁用管理员
     */
    @Log(module = "管理员", desc = "禁用管理员")
    @PreAuthorize("hasAnyAuthority('SystemADMIN','SuperAdmin','OrdinaryAdmin','system')")
    @PutMapping("/setAdminNotEnabled")
    public Result<Boolean> setAdminNotEnabled(@RequestParam Long adminId,@RequestParam Integer pageNum){
        return adminManagementService.setAdminNotEnabled(adminId,pageNum);
    }

    /**
     * 编辑管理员(修改权限、信息)
     */
    @Log(module = "管理员", desc = "修改管理员信息")
    @PreAuthorize("hasAnyAuthority('SystemADMIN','SuperAdmin','OrdinaryAdmin','system')")
    @PutMapping("/updateAdmin")
    public Result<Boolean> updateAdmin(@RequestBody AdminDTO adminDTO,@RequestParam Integer pageNum){
        return adminManagementService.updateAdmin(adminDTO,pageNum);
    }

    /**
     * 删除管理员
     */
    @Log(module = "管理员", desc = "删除管理员")
    @PreAuthorize("hasAnyAuthority('SystemADMIN','SuperAdmin','system')")
    @DeleteMapping("/deleteAdmin/{adminId}")
    public Result<Boolean> deleteAdmin(@PathVariable Long adminId){
        return adminManagementService.deleteAdmin(adminId);
    }

    /**
     *按管理员id取用户名（其他服务需要操作人/审计名时使用）
     */
    @PreAuthorize("hasAnyAuthority('SystemADMIN','system')")
    @GetMapping("/getAdminNameById")
    public Result<String> getAdminNameById(@RequestParam Long id){
        return adminManagementService.getAdminNameById(id);
    }
}
