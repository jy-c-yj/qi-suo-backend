package com.cyj.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyj.entity.ProductCategory;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 商品分类关联表 Mapper 接口
 * </p>
 *
 * @author cyj
 * @since 2026-01-27
 */
//extends BaseMapper<ProductCategory>
@Mapper
public interface ProductCategoryMapper extends BaseMapper<ProductCategory> {
}
