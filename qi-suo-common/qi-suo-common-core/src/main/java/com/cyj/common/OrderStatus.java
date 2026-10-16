package com.cyj.common;

/**
 * 订单状态口径。
 * 库里 status 是 varchar，且既有数据存的就是这些中文字面量，所以用常量而不是枚举
 * （枚举会牵动 Orders.status 的 TypeHandler 映射，改动面不划算）。
 *
 * 注意：统一用「未支付」，不再使用历史上的「待支付」。
 */
public final class OrderStatus {

    private OrderStatus() {
    }

    public static final String UNPAID = "未支付";
    public static final String PAID = "已支付";
    public static final String SHIPPED = "已发货";
    public static final String COMPLETED = "已完成";
    public static final String CANCELLED = "已取消";
}
