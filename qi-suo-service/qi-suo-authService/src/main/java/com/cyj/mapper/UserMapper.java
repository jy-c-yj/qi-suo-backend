package com.cyj.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyj.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户表 Mapper 接口（认证服务只做注册/登录读写，只用内建方法）
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
