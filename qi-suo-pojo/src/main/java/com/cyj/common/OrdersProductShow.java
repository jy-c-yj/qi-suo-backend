package com.cyj.common;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrdersProductShow {
    private Integer productId;
    private String productName;
    private String productImageUrl;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;
}
