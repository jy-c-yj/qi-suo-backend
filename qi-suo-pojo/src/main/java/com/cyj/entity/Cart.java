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
 * 购物车表
 * </p>
 *
 * @author cyj
 * @since 2026-03-11
 */
@Getter
@Setter
@TableName("cart")
public class Cart implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 商品ID
     */
    @TableField("product_id")
    private Long productId;
    /**
     * 商家ID
     */
    @TableField("merchant_id")
    private Long merchantId;

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
     * 总价
     */
    @TableField("total_price")
    private BigDecimal totalPrice;

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
