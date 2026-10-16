package com.cyj.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyj.dto.BackgroundGetProductByConditionDTO;
import com.cyj.dto.QueryProductByConditionDTO;
import com.cyj.dvo.BackgroundGetProductDVO;
import com.cyj.dvo.BackgroundGetProductDetailDVO;
import com.cyj.dvo.HotProductDVO;
import com.cyj.dvo.QueryProductDVO;
import com.cyj.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

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
     * 条件扣减库存。返回影响行数，0 表示库存不足（
     */
    @Update("UPDATE product SET stock = stock - #{quantity} WHERE id = #{productId} AND stock >= #{quantity}")
    int deductStock(@Param("productId") Long productId, @Param("quantity") Integer quantity);

    /**
     * 归还库存。
     */
    @Update("UPDATE product SET stock = COALESCE(stock, 0) + #{quantity} WHERE id = #{productId}")
    int restoreStock(@Param("productId") Long productId, @Param("quantity") Integer quantity);

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


    /**
     * 获取热门商品
     * @param page
     * @return
     */
    List<HotProductDVO> queryHotProduct(Page<HotProductDVO> page);

    /**
     * 根据条件查询产品列表
     *
     * @param page 分页对象，包含分页信息
     * @param query 查询条件对象，封装了查询参数
     * @return 返回符合条件的产品列表，结果已进行分页处理
     */
    List<QueryProductDVO> queryProductByCondition(Page<QueryProductDVO> page, QueryProductByConditionDTO query);
}
