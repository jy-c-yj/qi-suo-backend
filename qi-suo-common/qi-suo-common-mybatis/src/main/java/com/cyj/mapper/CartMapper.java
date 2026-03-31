package com.cyj.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyj.entity.Cart;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 购物车表 Mapper 接口
 * </p>
 *
 * @author cyj
 * @since 2026-03-11
 */

@Mapper
public interface CartMapper extends BaseMapper<Cart> {

}
