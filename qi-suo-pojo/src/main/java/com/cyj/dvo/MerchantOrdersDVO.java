package com.cyj.dvo;

import com.cyj.entity.Orders;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MerchantOrdersDVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 订单总价
     */
    private BigDecimal totalAmount;

    /**
     * 订单状态：未支付、已支付、已完成、已取消等
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    public static   MerchantOrdersDVO toMerchantOrdersDVO(Orders orders){
        return MerchantOrdersDVO.builder()
                .id(orders.getId())
                .orderNo(orders.getOrderNo())
                .totalAmount(orders.getTotalAmount())
                .status(orders.getStatus())
                .createdAt(orders.getCreatedAt())
                .build();
    }

}
