package com.cyj.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cyj.common.httpType;
import com.cyj.domain.LoginUserDetails;
import com.cyj.entity.Role;
import com.cyj.entity.User;
import com.cyj.entity.UserRole;
import com.cyj.mapper.RoleMapper;
import com.cyj.mapper.UserMapper;
import com.cyj.mapper.UserRoleMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    HttpServletRequest request;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LoginUserDetails loginUserDetails = new LoginUserDetails();
        String url = request.getRequestURI();
        if(url.contains(httpType.user)){
            System.out.println("用户登陆");
        }
        if (url.contains(httpType.admin)){
            System.out.println("管理员登陆");
        }
        if (url.contains(httpType.merchant)){
            System.out.println("商家登陆");
        }
        //从数据库获取用户信息
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }


        //从数据库获取用户权限信息
        QueryWrapper<UserRole> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("user_id", user.getId());
        List<UserRole> userRoles = userRoleMapper.selectList(queryWrapper1);
        List<Role> roles = new ArrayList<>();
        userRoles.forEach(userRole -> {
            roles.add(roleMapper.selectById(userRole.getRoleId()));
        });

        loginUserDetails.setAuthorities(roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())).toList());

        loginUserDetails.setPassword(user.getPassword());
        loginUserDetails.setUsername(user.getUsername());

        return loginUserDetails;
    }
}
