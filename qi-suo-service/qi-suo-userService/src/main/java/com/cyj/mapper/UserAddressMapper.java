package com.cyj.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyj.entity.UserAddress;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户地址表 Mapper 接口
 * </p>
 *
 * @author cyj
 * @since 2026-03-11
 */

@Mapper
public interface UserAddressMapper extends BaseMapper<UserAddress> {

}
