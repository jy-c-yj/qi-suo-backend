package com.cyj.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cyj.common.OrderStatus;
import com.cyj.common.OrderStockState;
import com.cyj.common.Result;
import com.cyj.config.OrderProperties;
import com.cyj.dto.ProductStockDTO;
import com.cyj.dto.ProductStockItemDTO;
import com.cyj.entity.OrderItem;
import com.cyj.feign.merchant.MerchantProductStockFeignClient;
import com.cyj.mapper.OrderItemMapper;
import com.cyj.mapper.OrdersMapper;
import com.cyj.service.OrderTimeoutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 订单超时取消
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OrderTimeoutServiceImpl implements OrderTimeoutService {


    private static final long COMPENSATE_STALE_SECONDS = 60L;

    private final OrdersMapper ordersMapper;
    private final OrderItemMapper orderItemMapper;
    private final MerchantProductStockFeignClient productStockFeignClient;
    private final OrderProperties orderProperties;

    @Override
    public boolean cancelUnpaidOrder(Long orderId) {
        if (orderId == null) {
            return false;
        }
        if (ordersMapper.casStatus(orderId, OrderStatus.UNPAID, OrderStatus.CANCELLED) != 1) {
            log.info("订单无需取消（已被支付/已取消）, orderId={}", orderId);
            return false;
        }
        // 没占用过库存或已归还没有后续
        Integer stockState = ordersMapper.selectStockState(orderId);
        if (stockState == null || stockState != OrderStockState.RESERVED) {
            log.info("订单 {} 无需归还库存, stockState={}", orderId, stockState);
            return true;
        }

        return restoreStock(orderId);
    }

    @Override
    public int sweepTimeoutOrders() {
        // 时间比较必须写在 SQL 里用 NOW()：JDBC URL 是 serverTimezone=UTC 而 JVM 是 Asia/Shanghai，
        // 用 Java 算出来的截止时间会整体偏 8 小时
        long seconds = Math.max(orderProperties.getTimeout().getSeconds(), 1L);
        List<Long> orderIds = ordersMapper.selectTimeoutOrderIds(
                OrderStatus.UNPAID, seconds, orderProperties.getSweepBatchSize());
        int cancelled = 0;
        for (Long orderId : orderIds) {
            if (cancelUnpaidOrder(orderId)) {
                cancelled++;
            }
        }
        return cancelled;
    }

    @Override
    public int compensateStockRestore() {
        List<Long> orderIds = ordersMapper.selectStockPendingIds(
                OrderStatus.CANCELLED, OrderStockState.RESERVED,
                COMPENSATE_STALE_SECONDS, orderProperties.getSweepBatchSize());
        int restored = 0;
        for (Long orderId : orderIds) {
            if (restoreStock(orderId)) {
                restored++;
            }
        }
        return restored;
    }

    /**
     * 归还订单占用的库存。成功后才把 stockState 从 1 CAS 到 2，保证只归还得成一次。
     */
    private boolean restoreStock(Long orderId) {
        List<OrderItem> items = orderItemMapper.selectList(new QueryWrapper<OrderItem>().eq("order_id", orderId));
        List<ProductStockItemDTO> stockItems = items.stream()
                .filter(item -> item.getProductId() != null && item.getQuantity() != null)
                .map(item -> new ProductStockItemDTO(item.getProductId(), item.getQuantity()))
                .toList();
        if (!stockItems.isEmpty()) {
            Result<String> result;
            try {
                result = productStockFeignClient.restoreStock(new ProductStockDTO(orderId, stockItems));
            } catch (Exception e) {
                log.error("归还库存调用异常, orderId={}", orderId, e);
                return false;
            }
            // Sentinel 降级返回的是 Result.error 而不是抛异常，必须判 code（1=成功 0=失败）
            if (result == null || !Integer.valueOf(1).equals(result.getCode())) {
                log.error("归还库存失败, orderId={}, resp={}", orderId, result);
                return false;
            }
        }
        ordersMapper.markStockState(orderId, OrderStockState.RESERVED, OrderStockState.RESTORED);
        log.info("订单 {} 库存已归还", orderId);
        return true;
    }
}
