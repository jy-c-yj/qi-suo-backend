package com.cyj.dvo;

import com.cyj.entity.Cart;
import com.cyj.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 购物车表
 * </p>
 *
 * @author cyj
 * @since 2026-03-11
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 商品ID
     */

    private Long productId;

    /**
     * 商品名
     */
    private String productName;

    /**
     * 商品图片url
     */
    private String productImageUrl;

    /**
     * 单价
     */

    private BigDecimal price;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 总价
     */
    private BigDecimal totalPrice;


    public static   CartDVO newCartDVO(Cart cart, Product product){
        return new CartDVO(cart.getId(),
                cart.getProductId(),
                product.getProductName(),
                product.getProductImageUrl(),
                product.getPrice(),
                cart.getQuantity(),
                cart.getTotalPrice());
    }

}
