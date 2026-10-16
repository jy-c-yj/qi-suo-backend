package com.cyj.common;

/**
 * 订单占用的库存状态（orders.stock_state）。
 *
 * 极性刻意设计成默认 0 = 未占用：这样本功能上线前的存量订单天然是 0，不需要任何数据迁移。
 * 若反过来定义成「0=待归还」，就必须手工 UPDATE 一遍存量行，漏做会让历史订单被补偿扫描反复加库存。
 */
public final class OrderStockState {

    private OrderStockState() {
    }

    /** 未占用库存：存量订单，或任何非下单流程写入的订单 */
    public static final int NOT_RESERVED = 0;

    /** 已占用、待归还 */
    public static final int RESERVED = 1;

    /** 已归还 */
    public static final int RESTORED = 2;
}
