package com.cyj.controller;


import com.cyj.Annotation.Log;
import com.cyj.common.Result;
import com.cyj.dto.UserAddressDTO;
import com.cyj.dto.UserInformationUpdateDTO;
import com.cyj.dvo.UserAddressDVO;
import com.cyj.dvo.UserInformationDVO;
import com.cyj.service.UserInformationManagementService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/userInformationManagement")
public class UserInformationManagementController {
    @Autowired
    private UserInformationManagementService userInformationManagementService;
    //获取用户信息
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/getUserInformation")
    public Result<UserInformationDVO> getUserInformation(){
        return userInformationManagementService.getUserInformation();
    }
    //修改用户信息
    @Log(module = "用户信息", desc = "修改个人信息")
    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/updateUserInformation")
    public Result<Boolean> updateUserInformation(@RequestBody UserInformationUpdateDTO userInformationUpdateDTO){
        return userInformationManagementService.updateUserInformation(userInformationUpdateDTO);
    }
    //新增用户地址
    @Log(module = "用户信息", desc = "新增收货地址")
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/addUserAddress")
    public Result<Boolean> addUserAddress(@RequestBody UserAddressDTO userAddressDTO){
        return userInformationManagementService.addUserAddress(userAddressDTO);
    }

    //修改用户地址
    @Log(module = "用户信息", desc = "修改收货地址")
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/updateUserAddress")
    public Result<Boolean> updateUserAddress(@RequestBody UserAddressDTO userAddressDTO){
        return userInformationManagementService.updateUserAddress(userAddressDTO);
    }

    //删除用户地址
    @Log(module = "用户信息", desc = "删除收货地址")
    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/deleteUserAddress/{id}")
    public Result<Boolean> deleteUserAddress(@PathVariable Long id){
        return userInformationManagementService.deleteUserAddress(id);
    }

    //查询用户地址
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/getUserAddress")
    public Result<List<UserAddressDVO>> getUserAddress(){
        return userInformationManagementService.getUserAddress();
    }
}
