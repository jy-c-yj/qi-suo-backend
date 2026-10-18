package com.cyj.service;

import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dvo.MerchantOrdersDVO;
import com.cyj.dvo.UserOrdersDVO;
import com.cyj.dvo.UserOrdersDetailDVO;

import java.util.List;

public interface UserOrdersService {
    /**
     * 当前用户的订单列表
     *
     * @param status 可选的状态过滤；为空表示全部
     */
    Result<List<UserOrdersDVO>> list(String status);

    Result<UserOrdersDetailDVO> detail(Long orderId);

    Result<String> pay(Long orderId);

    Result<String> confirm(Long orderId);

    /**
     * 商家维度的订单列表（供商家服务经服务间调用使用）
     *
     * @param status 可选的状态过滤；为空表示全部
     * @param sort   时间排序：asc 最早在前，其余（默认）最新在前
     */
    PageResult<MerchantOrdersDVO> merchantList(Long merchantId, Integer pageNum, Integer pageSize,
                                               String status, String sort);

    /**
     * 商家发货：已支付 -> 已发货（供商家服务经服务间调用使用）
     */
    Result<String> setOrdersStatus(Long orderId);
}
