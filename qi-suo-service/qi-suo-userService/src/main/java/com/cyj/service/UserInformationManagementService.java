package com.cyj.service;

import com.cyj.common.Result;
import com.cyj.dto.UserAddressDTO;
import com.cyj.dto.UserInformationUpdateDTO;
import com.cyj.dvo.UserAddressDVO;
import com.cyj.dvo.UserInformationDVO;

import java.util.List;

public interface UserInformationManagementService {
    /**
     * 获取用户信息的方法
     */
    Result<UserInformationDVO> getUserInformation();

    Result<Boolean> updateUserInformation(UserInformationUpdateDTO userInformationUpdateDTO);

    Result<Boolean> addUserAddress(UserAddressDTO userAddressDTO);

    Result<Boolean> updateUserAddress(UserAddressDTO userAddressDTO);

    Result<Boolean> deleteUserAddress(Long id);

    Result<List<UserAddressDVO>> getUserAddress();

}
