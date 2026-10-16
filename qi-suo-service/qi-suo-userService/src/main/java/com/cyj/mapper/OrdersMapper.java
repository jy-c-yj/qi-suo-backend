package com.cyj.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyj.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author cyj
 * @since 2026-03-11
 */
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {

    /**
     * 状态 CAS。返回影响行数，1 代表本次调用拿到了这行订单的所有权。
     * 超时取消、支付、发货全部收敛到这一条语句上，靠 MySQL 的行锁 + 当前读保证只有一个赢家。
     */
    @Update("UPDATE orders SET status = #{to} WHERE id = #{orderId} AND status = #{from}")
    int casStatus(@Param("orderId") Long orderId, @Param("from") String from, @Param("to") String to);

    /**
     * 库存状态 CAS（1 -> 2），保证库存只归还一次。
     */
    @Update("UPDATE orders SET stock_state = #{to} WHERE id = #{orderId} AND stock_state = #{from}")
    int markStockState(@Param("orderId") Long orderId, @Param("from") int from, @Param("to") int to);

    @Select("SELECT stock_state FROM orders WHERE id = #{orderId}")
    Integer selectStockState(@Param("orderId") Long orderId);

    /**
     * 超时未支付的订单 id。
     * 时间比较必须用 NOW()：JDBC URL 带 serverTimezone=UTC 而 JVM 是 Asia/Shanghai，
     * 拿 Java 算出来的 LocalDateTime 去比 created_at 会整体偏 8 小时。
     */
    @Select("SELECT id FROM orders WHERE status = #{status} "
            + "AND created_at <= DATE_SUB(NOW(), INTERVAL #{seconds} SECOND) "
            + "ORDER BY created_at LIMIT #{limit}")
    List<Long> selectTimeoutOrderIds(@Param("status") String status,
                                     @Param("seconds") long seconds,
                                     @Param("limit") int limit);

    /**
     * 已取消但库存还没归还成功的订单 id（Feign 调用失败时留给补偿扫描用）。
     */
    @Select("SELECT id FROM orders WHERE status = #{status} AND stock_state = #{stockState} "
            + "AND updated_at <= DATE_SUB(NOW(), INTERVAL #{staleSeconds} SECOND) "
            + "ORDER BY updated_at LIMIT #{limit}")
    List<Long> selectStockPendingIds(@Param("status") String status,
                                     @Param("stockState") int stockState,
                                     @Param("staleSeconds") long staleSeconds,
                                     @Param("limit") int limit);
}
