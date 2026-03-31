package com.cyj.service;

import com.cyj.common.Result;
import com.cyj.dvo.UserOrdersDVO;
import com.cyj.dvo.UserOrdersDetailDVO;

import java.util.List;

public interface UserOrdersService {
    Result<List<UserOrdersDVO>> list();

    Result<UserOrdersDetailDVO> detail(Long orderId);

    Result<String> pay(Long orderId);

    Result<String> confirm(Long orderId);
}
