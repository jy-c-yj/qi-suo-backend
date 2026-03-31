package com.cyj.controller;


import com.cyj.common.Result;
import com.cyj.dto.UserAddressDTO;
import com.cyj.dto.UserInformationUpdateDTO;
import com.cyj.dvo.UserAddressDVO;
import com.cyj.dvo.UserInformationDVO;
import com.cyj.service.UserInformationManagementService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/userInformationManagement")
public class userInformationManagementController {
    @Autowired
    private UserInformationManagementService userInformationManagementService;
    //获取用户信息
    @GetMapping("/getUserInformation")
    public Result<UserInformationDVO> getUserInformation(){
        return userInformationManagementService.getUserInformation();
    }
    //修改用户信息
    @PutMapping("/updateUserInformation")
    public Result<Boolean> updateUserInformation(@RequestBody UserInformationUpdateDTO userInformationUpdateDTO){
        return userInformationManagementService.updateUserInformation(userInformationUpdateDTO);
    }
    //新增用户地址
    @PostMapping("/addUserAddress")
    public Result<Boolean> addUserAddress(@RequestBody UserAddressDTO userAddressDTO){
        return userInformationManagementService.addUserAddress(userAddressDTO);
    }

    //修改用户地址
    @PostMapping("/updateUserAddress")
    public Result<Boolean> updateUserAddress(@RequestBody UserAddressDTO userAddressDTO){
        return userInformationManagementService.updateUserAddress(userAddressDTO);
    }

    //删除用户地址
    @DeleteMapping("/deleteUserAddress/{id}")
    public Result<Boolean> deleteUserAddress(@PathVariable Long id){
        return userInformationManagementService.deleteUserAddress(id);
    }

    //查询用户地址
    @GetMapping("/getUserAddress")
    public Result<List<UserAddressDVO>> getUserAddress(){
        return userInformationManagementService.getUserAddress();
    }
}
