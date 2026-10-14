package com.cyj.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cyj.common.Result;
import com.cyj.dto.UserRegisterDTO;
import com.cyj.entity.User;
import com.cyj.entity.UserRole;
import com.cyj.mapper.UserMapper;
import com.cyj.mapper.UserRoleMapper;
import com.cyj.service.UserRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserRegisterServiceImpl implements UserRegisterService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> register(UserRegisterDTO userRegisterDTO) {

        String username = userRegisterDTO.getUsername();
        String password = userRegisterDTO.getPassword();
        String email = userRegisterDTO.getEmail();
        String phone = userRegisterDTO.getPhone();
        String imageUrl = userRegisterDTO.getImageUrl();

        //检查userRegisterDTO完整性
        if (username == null || password == null ||
                email == null || phone == null || imageUrl == null ||
                username.isEmpty() || password.isEmpty() ||
                email.isEmpty() || phone.isEmpty() || imageUrl.isEmpty()) {
            return Result.error("注册信息缺失");
        }
        //检查用户名是否重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);
        if(user != null){
            return Result.error("该用户已存在");
        }

        //用户存入数据库
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setEmail(userRegisterDTO.getEmail());
        newUser.setPhone(userRegisterDTO.getPhone());
        newUser.setImageUrl(userRegisterDTO.getImageUrl());
        userMapper.insert(newUser);

        //设置权限
        UserRole userRole = new UserRole();
        userRole.setUserId(newUser.getId());
        userRole.setRoleId(Long.parseLong("1"));
        userRole.setIsActive(true);
        userRoleMapper.insert(userRole);


        return Result.success("注册成功");
    }
}
