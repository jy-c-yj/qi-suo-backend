package com.cyj.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyj.entity.Product;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品表 Mapper 接口（用户服务只用内建方法，商品查询一律经 merchant 服务）
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}
