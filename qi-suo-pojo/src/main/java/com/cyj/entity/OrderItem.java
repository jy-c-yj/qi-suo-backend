package com.cyj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 订单项表
 * </p>
 *
 * @author cyj
 * @since 2026-03-11
 */
@Getter
@Setter
@TableName("order_item")
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属订单ID
     */
    @TableField("order_id")
    private Long orderId;

    /**
     * 商品ID
     */
    @TableField("product_id")
    private Long productId;

    /**
     * 单价
     */
    @TableField("price")
    private BigDecimal price;

    /**
     * 数量
     */
    @TableField("quantity")
    private Integer quantity;

    /**
     * 总价（单价*数量）
     */
    @TableField("total_price")
    private BigDecimal totalPrice;

    /**
     * 创建时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;
}
