package com.cyj.dvo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author cyj
 * @since 2026-03-11
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserOrdersDVO implements Serializable {

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
     * 商家名
     */
    private String merchantName;


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
