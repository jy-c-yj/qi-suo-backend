package com.cyj.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
public class NewProductDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品图片url
     */
    private String productImageUrl;

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
     * 设置分类
     */
    private List<Integer> categoryIds;

    /**
     * 设置属性
     */
    private List<Integer> attributeAndValues;
}
