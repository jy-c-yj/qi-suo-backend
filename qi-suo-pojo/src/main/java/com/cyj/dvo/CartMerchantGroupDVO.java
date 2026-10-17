package com.cyj.dvo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车按商家分组后的展示对象。
 * CartDVO 本身不含 merchantId，单独用一个分组对象补上商家信息。
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartMerchantGroupDVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 商家ID
     */
    private Long merchantId;

    /**
     * 商家名称
     */
    private String merchantName;

    /**
     * 该商家下的购物车项
     */
    private List<CartDVO> items;

    /**
     * 该商家购物车合计金额
     */
    private BigDecimal totalPrice;

    /**
     * 该商家购物车商品总件数
     */
    private Integer totalQuantity;
}
