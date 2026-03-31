package com.cyj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author cyj
 * @since 2026-03-11
 */
@Getter
@Setter
@TableName("orders")
public class Orders implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单号
     */
    @TableField("order_no")
    private String orderNo;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 用户地址ID
     */
    @TableField("user_address_id")
    private Long userAddressId;

    /**
     * 商家ID
     */
    @TableField("merchant_id")
    private Long merchantId;

    /**
     * 订单总价
     */
    @TableField("total_amount")
    private BigDecimal totalAmount;

    /**
     * 订单状态：未支付、已支付、已完成、已取消等
     */
    @TableField("status")
    private String status;

    /**
     * 创建时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
