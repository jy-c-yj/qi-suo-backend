package com.cyj.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyj.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 订单项表 Mapper 接口
 * </p>
 *
 * @author cyj
 * @since 2026-03-11
 */
@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {

}
