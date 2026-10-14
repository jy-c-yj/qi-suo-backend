package com.cyj.service;

import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dto.BackgroundGetProductByConditionDTO;
import com.cyj.dto.QueryProductByConditionDTO;
import com.cyj.dvo.BackgroundGetProductDVO;
import com.cyj.dvo.BackgroundGetProductDetailDVO;
import com.cyj.dvo.HotProductDVO;
import com.cyj.dvo.QueryProductDVO;

/**
 * 商品查询服务：商品的读接口统一由商家服务提供
 * （用户服务 / 管理服务的同名查询均通过服务间调用转到这里）
 */
public interface MerchantProductQueryService {

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
     */
    Result<BackgroundGetProductDetailDVO> queryProductDetail(Integer id);

    /**
     * 根据条件查询商品
     * @param queryProductByConditionDTO 包含查询条件的DTO对象
     * @return 返回一个包含商品分页结果的对象
     */
    Result<PageResult<QueryProductDVO>> queryProductByCondition(QueryProductByConditionDTO queryProductByConditionDTO);

    /**
     * 后台商品列表查询（不按商家归属过滤，供管理服务使用）
     * @param backgroundGetProductByConditionDTO 包含分页与查询条件
     * @return 返回商品分页结果
     */
    Result<PageResult<BackgroundGetProductDVO>> queryProductList(BackgroundGetProductByConditionDTO backgroundGetProductByConditionDTO);
}
