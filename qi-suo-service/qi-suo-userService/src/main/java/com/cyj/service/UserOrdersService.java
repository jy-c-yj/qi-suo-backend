package com.cyj.service;

import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dvo.MerchantOrdersDVO;
import com.cyj.dvo.UserOrdersDVO;
import com.cyj.dvo.UserOrdersDetailDVO;

import java.util.List;

public interface UserOrdersService {
    Result<List<UserOrdersDVO>> list();

    Result<UserOrdersDetailDVO> detail(Long orderId);

    Result<String> pay(Long orderId);

    Result<String> confirm(Long orderId);

    /**
     * 商家维度的订单列表（供商家服务经服务间调用使用）
     */
    PageResult<MerchantOrdersDVO> merchantList(Long merchantId, Integer pageNum, Integer pageSize);

    /**
     * 商家发货：已支付 -> 已发货（供商家服务经服务间调用使用）
     */
    Result<String> setOrdersStatus(Long orderId);
}
