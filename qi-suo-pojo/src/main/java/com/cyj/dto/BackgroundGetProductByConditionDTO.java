package com.cyj.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

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
public class BackgroundGetProductByConditionDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 商品ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 商家名
     */
    @TableField(value = "merchant_name")
    private String merchantName;

    /**
     * 商品名称
     */
    @TableField("product_name")
    private String productName;
    /**
     * 商品SKU
     */
    @TableField("sku")
    private String sku;

    /**
     * 商品状态：1-上架，0-下架
     */
    private Integer status;

    Integer pageNum;

    Integer pageSize;

}
