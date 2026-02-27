package com.cyj.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 商品属性值关联表
 * </p>
 *
 * @author cyj
 * @since 2026-01-27
 */
@Getter
@Setter
public class NewProductAttributeValueDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     */
    private Integer productId;

    /**
     * 属性值ID
     */

    private Integer attributeValueId;


    private LocalDateTime createdAt;
}
