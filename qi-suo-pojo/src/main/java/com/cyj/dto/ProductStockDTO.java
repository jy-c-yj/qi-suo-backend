package com.cyj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 用户服务调用商家服务库存扣减/归还的入参。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductStockDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long orderId;

    private List<ProductStockItemDTO> items;
}
