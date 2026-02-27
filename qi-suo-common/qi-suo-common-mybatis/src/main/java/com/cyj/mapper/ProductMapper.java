package com.cyj.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyj.dto.BackgroundGetProductByConditionDTO;
import com.cyj.dvo.BackgroundGetProductDVO;
import com.cyj.dvo.BackgroundGetProductDetailDVO;
import com.cyj.entity.Product;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 商品表 Mapper 接口
 * </p>
 *
 * @author cyj
 * @since 2026-01-27
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    /**
     * 查询商品列表
     * @param backgroundGetProductByConditionDTO
     * @return
     */
    Page<BackgroundGetProductDVO> getProductDVOList(Page<BackgroundGetProductDVO> page, BackgroundGetProductByConditionDTO backgroundGetProductByConditionDTO);

    /**
     * 获取商品详情
     * @param productId
     * @return
     */
    BackgroundGetProductDetailDVO selectProductDetailById(Integer productId);



}
