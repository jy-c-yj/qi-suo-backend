package com.cyj.service;

import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dvo.MerchantOrdersDVO;
import com.cyj.dvo.UserOrdersDetailDVO;

public interface MerchantOrdersService {
    PageResult<MerchantOrdersDVO> getOrders(Integer pageNum, Integer pageSize, String status, String sort);

    Result<UserOrdersDetailDVO> getOrdersDetail(Long orderId);

    Result<String> setOrdersStatus(Long orderId);
}
