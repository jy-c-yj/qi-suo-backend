package com.cyj.service.impl;

import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dvo.MerchantOrdersDVO;
import com.cyj.dvo.UserOrdersDetailDVO;
import com.cyj.feign.user.UserOrdersFeignClient;
import com.cyj.service.MerchantOrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * 订单数据归用户服务所有，本服务只做转发
 * （Feign 调用带的是固定身份，所以商家id必须从当前登录态取出后显式传参）
 */
@Service
@Slf4j
public class MerchantOrdersServiceImpl implements MerchantOrdersService {

    @Autowired
    private UserOrdersFeignClient userOrdersFeignClient;

    @Override
    public PageResult<MerchantOrdersDVO> getOrders(Integer pageNum, Integer pageSize, String status, String sort) {
        log.info("分页查询商家订单列表");

        //获取商家id
        Long merchantId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userOrdersFeignClient.merchantList(merchantId, pageNum, pageSize, status, sort);
    }

    @Override
    public Result<UserOrdersDetailDVO> getOrdersDetail(Long orderId) {
        return userOrdersFeignClient.detail(orderId);
    }

    @Override
    public Result<String> setOrdersStatus(Long orderId) {
        return userOrdersFeignClient.setStatus(orderId);
    }
}
