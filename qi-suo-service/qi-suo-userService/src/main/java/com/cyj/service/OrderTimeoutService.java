package com.cyj.service;

/**
 * 订单超时未支付的处理：取消 + 归还库存。
 */
public interface OrderTimeoutService {

    /**
     * 取消一笔未支付订单并归还其占用的库存。
     */
    boolean cancelUnpaidOrder(Long orderId);

    /**
     * 取消所有超过超时时长的未支付订单。
     */
    int sweepTimeoutOrders();

    /**
     * 补偿扫描：已取消但库存还没归还成功的订单（Feign 调用失败时）重试归还。
     */
    int compensateStockRestore();
}
