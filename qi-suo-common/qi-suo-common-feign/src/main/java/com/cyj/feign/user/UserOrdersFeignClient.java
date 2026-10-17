package com.cyj.feign.user;


import com.cyj.back.UserOrdersFeignClientFallBack;
import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dvo.MerchantOrdersDVO;
import com.cyj.dvo.UserOrdersDetailDVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 商家服务 -> 用户服务：订单数据归用户服务所有
 */
@FeignClient(value = "qi-suo-user",
        contextId = "userOrdersFeignClient",
        fallback = UserOrdersFeignClientFallBack.class)
@Primary
public interface UserOrdersFeignClient {

    /**
     * 商家维度的订单列表（商家id需显式传入）
     */
    @GetMapping("/api/user/orders/merchantList")
    PageResult<MerchantOrdersDVO> merchantList(@RequestParam("merchantId") Long merchantId,
                                               @RequestParam("pageNum") Integer pageNum,
                                               @RequestParam("pageSize") Integer pageSize,
                                               @RequestParam(value = "status", required = false) String status,
                                               @RequestParam(value = "sort", required = false) String sort);

    /**
     * 订单详情
     */
    @GetMapping("/api/user/orders/detail")
    Result<UserOrdersDetailDVO> detail(@RequestParam("orderId") Long orderId);

    /**
     * 商家发货：已支付 -> 已发货
     */
    @GetMapping("/api/user/orders/setStatus")
    Result<String> setStatus(@RequestParam("orderId") Long orderId);
}
