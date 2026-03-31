package com.cyj.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.*;

/**
 * <p>
 * 商品属性值关联表
 * </p>
 *
 * @author cyj
 * @since 2026-01-27
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductAttributeValue implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     */
    @TableField("product_id")
    private Integer productId;

    /**
     * 属性值ID
     */
    @TableField("attribute_value_id")
    private Integer attribute_valueId;

    @TableField("created_at")
    private LocalDateTime createdAt;

}
