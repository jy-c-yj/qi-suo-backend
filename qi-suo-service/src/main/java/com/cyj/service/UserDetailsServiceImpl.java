package com.cyj.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cyj.common.httpType;
import com.cyj.constants.RoleConstants;
import com.cyj.domain.LoginUserDetails;
import com.cyj.entity.*;
import com.cyj.mapper.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    HttpServletRequest request;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LoginUserDetails loginUserDetails = new LoginUserDetails();
        String url = request.getRequestURI();


        if(url.contains(httpType.user)){
            log.info("用户登陆 ---> {}",username);
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
            System.out.println("用户登陆");
        }

        if (url.contains(httpType.admin)){
            log.info("管理员登陆 ---> {}",username);
            //从数据库获取用户信息
            QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", username);
            Admin admin = adminMapper.selectOne(queryWrapper);
            if (admin == null) {
                throw new UsernameNotFoundException(username);
            }
            loginUserDetails.setAuthorities(Stream.of(admin.getAdminType()).map(type -> {
                if(type == 1){
                    return new SimpleGrantedAuthority(RoleConstants.OrdinaryADMIN);
                }else if(type == 2){
                    return new SimpleGrantedAuthority(RoleConstants.SuperADMIN);
                }
                return new SimpleGrantedAuthority(RoleConstants.ROLE_ERROR);
            }).collect(Collectors.toList()));
            loginUserDetails.setPassword(admin.getPassword());
            loginUserDetails.setUsername(admin.getUsername());
            System.out.println("管理员登陆");
        }

        if (url.contains(httpType.merchant)){
            log.info("商家登陆 ---> {}",username);
            //从数据库获取用户信息
            QueryWrapper<Merchant> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("Merchant_name", username);
            Merchant merchant = merchantMapper.selectOne(queryWrapper);
            if (merchant == null) {
                throw new UsernameNotFoundException(username);
            }

            loginUserDetails.setAuthorities(List.of(new SimpleGrantedAuthority(RoleConstants.MERCHANT)));
            loginUserDetails.setPassword(merchant.getMerchantPassword());
            loginUserDetails.setUsername(merchant.getMerchantName());

        }

        return loginUserDetails;
    }
}
