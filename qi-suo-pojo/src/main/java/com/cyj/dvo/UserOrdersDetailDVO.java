package com.cyj.dvo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cyj.common.OrdersProductShow;
import com.cyj.entity.Product;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserOrdersDetailDVO implements Serializable {

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
     * 用户地址ID
     */
    private Long userAddressId;

    /**
     * 用户地址详情
     */
    private String detailAddress;

    /**
     * 商家ID
     */
    private Long merchantId;

    /**
     * 商家名
     */
    private String merchantName;

    /**
     * 商品列表
     */
    private List<OrdersProductShow> productList;

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

}
