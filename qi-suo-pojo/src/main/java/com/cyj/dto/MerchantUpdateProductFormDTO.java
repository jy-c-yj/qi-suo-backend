package com.cyj.dto;

import com.cyj.common.AttributeAndValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class MerchantUpdateProductFormDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     */

    private Integer id;


    /**
     * 商品名称
     */

    private String productName;


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
     * 是否新品
     */

    private Boolean isNew;


}
