package com.cyj.service;

import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dto.QueryProductByConditionDTO;
import com.cyj.dvo.BackgroundGetProductDetailDVO;
import com.cyj.dvo.HotProductDVO;
import com.cyj.dvo.QueryProductDVO;

public interface UserProductService {
    /**
     * 查询热门产品列表
     * @param pageNum 当前页码
     * @param pageSize 每页显示数量
     * @return 返回包含热门产品数据分页结果的对象
     */
    Result<PageResult<HotProductDVO>> queryHotProduct(Integer pageNum, Integer pageSize);

    /**
     * 查询产品详情的方法
     * @param id 产品ID，用于标识要查询的具体产品
     * @return 返回一个Result对象，其中包含BackgroundGetProductDetailDVO类型的数据
     *         BackgroundGetProductDetailDVO中包含了产品的详细信息
     */
    Result<BackgroundGetProductDetailDVO> queryProductDetail(Integer id);

    /**
     * 根据条件查询商品
     * @param queryProductByConditionDTO 包含查询条件的DTO对象
     * @return 返回一个包含商品分页结果的对象
     */
    Result<PageResult<QueryProductDVO>> queryProductByCondition(QueryProductByConditionDTO queryProductByConditionDTO);
}
