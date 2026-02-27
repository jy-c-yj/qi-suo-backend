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
 * 商品表
 * </p>
 *
 * @author cyj
 * @since 2026-01-27
 */
@Getter
@Setter
@TableName("product")
public class Product implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商户ID
     */
    @TableField("merchant_id")
    private Long merchantId;

    /**
     * 商品名称
     */
    @TableField("product_name")
    private String productName;

    /**
     * 商品图片url
     */
    @TableField("product_image_url")
    private String productImageUrl;

    /**
     * 详细描述
     */
    @TableField("description")
    private String description;

    /**
     * 销售价格
     */
    @TableField("price")
    private BigDecimal price;

    /**
     * 库存数量
     */
    @TableField("stock")
    private Integer stock;

    /**
     * 原价
     */
    @TableField("original_price")
    private BigDecimal originalPrice;

    /**
     * 商品SKU
     */
    @TableField("sku")
    private String sku;


    /**
     * 是否热销
     */
    @TableField("is_hot")
    private Boolean isHot;

    /**
     * 是否新品
     */
    @TableField("is_new")
    private Boolean isNew;

    /**
     * 状态：1-上架，2-下架
     */
    @TableField("status")
    private Byte status;

    /**
     * 平均评分
     */
    @TableField("rating")
    private BigDecimal rating;

    /**
     * 销售数量
     */
    @TableField("sales_count")
    private Integer salesCount;

    /**
     * 浏览次数
     */
    @TableField("view_count")
    private Integer viewCount;


    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;

    /**
     * 是否删除：0-未删除，1-已删除
     */
    @TableField("is_deleted")
    private Boolean isDeleted;

}
