package com.cyj.dvo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 分类表
 * </p>
 *
 * @author cyj
 * @since 2026-01-27
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BackgroundGetProductDVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     */

    private Integer id;

    /**
     * 商家名
     */

    private String merchantName;

    /**
     * 商品名称
     */

    private String productName;

    /**
     * 商品图片url
     */
    private String productImageUrl;

    /**
     * 销售价格
     */

    private BigDecimal price;

    /**
     * 库存数量
     */

    private Integer stock;

    /**
     * 原价
     */

    private BigDecimal originalPrice;

    /**
     * 商品SKU
     */

    private String sku;

    /**
     * 状态：1-上架，2-下架
     */

    private Byte status;


    /**
     * 销售数量
     */

    private Integer salesCount;

    /**
     * 浏览次数
     */

    private Integer viewCount;



    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;
}
