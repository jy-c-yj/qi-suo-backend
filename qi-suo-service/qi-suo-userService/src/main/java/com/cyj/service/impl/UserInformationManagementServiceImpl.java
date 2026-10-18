package com.cyj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cyj.common.Result;
import com.cyj.constant.RedissonKey;
import com.cyj.dto.ChangePasswordDTO;
import com.cyj.dto.UserAddressDTO;
import com.cyj.dto.UserInformationUpdateDTO;
import com.cyj.dvo.UserAddressDVO;
import com.cyj.dvo.UserInformationDVO;
import com.cyj.entity.User;
import com.cyj.entity.UserAddress;
import com.cyj.mapper.UserAddressMapper;
import com.cyj.mapper.UserMapper;
import com.cyj.service.UserInformationManagementService;
import com.cyj.utils.RedissonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;




/**
 * 用户信息管理服务实现类
 * 实现UserInformationManagementService接口，提供用户信息相关的业务逻辑处理
 * 使用@Service注解标记为Spring服务组件
 * 使用@Slf4j注解自动生成日志记录器，便于日志记录
 */
@Service
@Slf4j
public class UserInformationManagementServiceImpl implements UserInformationManagementService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserAddressMapper userAddressMapper;
    @Autowired
    private RedissonUtil redissonUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    /**
     * 获取用户信息的方法
     */
    @Override
    public Result<UserInformationDVO> getUserInformation() {
        log.info("获取用户信息");
        //获取用户id
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //根据用户id查询用户信息
        if(userId == null){
            return Result.error("获取用户id失败，用户不存在");
        }
        //从缓存获取
        String key = RedissonKey.INFORMATION_USER_KEY + userId;
        UserInformationDVO userInformationDVO = redissonUtil.getValue(key);

        //如果缓存中没有，则从数据库中获取
        if(userInformationDVO == null){
            userInformationDVO = UserInformationDVO.toUserInformationDVO(userMapper.selectById(userId));
        }
        //密码永不回传前端（同时覆盖 Redis 中的历史缓存）
        if (userInformationDVO != null) {
            userInformationDVO.setPassword(null);
            redissonUtil.setValue(key, userInformationDVO, 7, TimeUnit.DAYS);
        }
        return Result.success(userInformationDVO);

    }

    @Override
    public Result<Boolean> changePassword(ChangePasswordDTO changePasswordDTO) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userId == null) {
            return Result.error("获取用户id失败，用户不存在");
        }
        if (changePasswordDTO == null
                || changePasswordDTO.getOldPassword() == null || changePasswordDTO.getOldPassword().isBlank()
                || changePasswordDTO.getNewPassword() == null || changePasswordDTO.getNewPassword().isBlank()) {
            return Result.error("请输入旧密码和新密码");
        }
        User user = userMapper.selectById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }
        if (!matchesPassword(changePasswordDTO.getOldPassword(), user.getPassword())) {
            return Result.error("旧密码不正确");
        }
        User update = new User();
        update.setId(userId);
        update.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userMapper.updateById(update);
        // 旧缓存里可能带密码，直接删除
        redissonUtil.delete(RedissonKey.INFORMATION_USER_KEY + userId);
        return Result.success(true);
    }

    /**
     * 兼容有 {id} 前缀的加密密码与历史明文密码
     */
    private boolean matchesPassword(String rawPassword, String storedPassword) {
        if (storedPassword == null) {
            return false;
        }
        try {
            return passwordEncoder.matches(rawPassword, storedPassword);
        } catch (IllegalArgumentException e) {
            return storedPassword.equals(rawPassword);
        }
    }

    @Override
    public Result<Boolean> updateUserInformation(UserInformationUpdateDTO userInformationUpdateDTO) {
        log.info("更新用户信息");
        //获取用户id
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //根据用户id查询用户信息
        if(userId == null){
            return Result.error("获取用户id失败，用户不存在");
        }
        //更新用户信息
        userMapper.updateById(UserInformationUpdateDTO.toUser(userInformationUpdateDTO,userId));
        //删除缓存
        String key = RedissonKey.INFORMATION_USER_KEY + userId;
        redissonUtil.delete(key);
        return Result.success(true);
    }

    @Override
    public Result<Boolean> addUserAddress(UserAddressDTO userAddressDTO) {
        //新增地址
        UserAddress  userAddress = new UserAddress();
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userAddress.setDetailAddress(userAddressDTO.getDetailAddress());
        userAddress.setUserId(userId);
        userAddress.setIsDefault(false);
        userAddressMapper.insert(userAddress);
        return Result.success("新增成功");
    }

    @Override
    public Result<Boolean> updateUserAddress(UserAddressDTO userAddressDTO) {
        UserAddress userAddress = new UserAddress();
        userAddress.setId(userAddressDTO.getId());
        userAddress.setDetailAddress(userAddressDTO.getDetailAddress());
        userAddressMapper.updateById(userAddress);
        return Result.success("修改成功");
    }

    @Override
    public Result<Boolean> deleteUserAddress(Long id) {
        userAddressMapper.deleteById(id);
        return Result.success("删除成功");
    }

    @Override
    public Result<List<UserAddressDVO>> getUserAddress() {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<UserAddress> userAddressList = userAddressMapper.selectList(new QueryWrapper<UserAddress>().eq("user_id",userId));
        List<UserAddressDVO> userAddressDVOList = userAddressList.stream().map(address -> {
            UserAddressDVO userAddressDVO = new UserAddressDVO();
            userAddressDVO.setId(address.getId());
            userAddressDVO.setDetailAddress(address.getDetailAddress());
            return userAddressDVO;
        }).toList();
        return Result.success(userAddressDVOList);
    }
}
