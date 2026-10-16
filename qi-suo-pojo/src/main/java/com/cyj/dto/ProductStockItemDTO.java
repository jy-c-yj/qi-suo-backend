package com.cyj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 单个商品的库存变更明细。
 * productId 用 Long 对齐 OrderItem.productId（product.id 是 Integer，SQL 里靠 MySQL 隐式比较）。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductStockItemDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long productId;

    private Integer quantity;
}
