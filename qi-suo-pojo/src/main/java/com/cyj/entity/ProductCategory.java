package com.cyj.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 商品分类关联表
 * </p>
 *
 * @author cyj
 * @since 2026-01-27
 */
@Getter
@Setter
@TableName("product_category")
public class ProductCategory implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     */
    @TableField("product_id")
    private Integer productId;

    /**
     * 分类ID
     */
    @TableField("category_id")
    private Integer categoryId;

    @TableField("created_at")
    private LocalDateTime createdAt;
}
