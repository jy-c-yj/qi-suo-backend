package com.cyj.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyj.entity.ProductAttributeValue;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 商品属性值关联表 Mapper 接口
 * </p>
 *
 * @author cyj
 * @since 2026-01-27
 */
//extends BaseMapper<ProductAttributeValue>
@Mapper
public interface ProductAttributeValueMapper extends BaseMapper<ProductAttributeValue>{

}
