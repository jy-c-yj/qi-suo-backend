package com.cyj.dvo;

import com.cyj.common.AttributeAndValue;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 商品表
 * </p>
 *
 * @author cyj
 * @since 2026-01-27
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BackgroundGetProductDetailDVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     */

    private Integer id;

    /**
     * 商户ID
     */

    private Integer merchantId;

    /**
     * 商品名称
     */

    private String productName;

    /**
     * 商家名
     */

    private String merchantName;

    /**
     * 详细描述
     */

    private String description;

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
     * 是否热销
     */

    private Boolean isHot;

    /**
     * 是否新品
     */

    private Boolean isNew;

    /**
     * 状态：1-上架，2-下架
     */

    private Byte status;

    /**
     * 平均评分
     */

    private BigDecimal rating;

    /**
     * 销售数量
     */

    private Integer salesCount;

    /**
     * 浏览次数
     */

    private Integer viewCount;

    /**
     * 分类名称
     */

    private List<String> categoryNames;

    /**
     * 属性名称and属性值
     */
    private List<AttributeAndValue> attributeAndValues;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
