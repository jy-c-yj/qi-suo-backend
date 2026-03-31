package com.cyj.dvo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Savepoint;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotProductDVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     */
    private Integer id;
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 所属商家id
     */
    private Integer merchantId;
    /**
     * 所属商家名称
     */
    private String merchantName;
    /**
     * 商品图片url
     */
    private String productImageUrl;
    /**
     * 销售价格
     */
    private BigDecimal price;
    /**
     * 库存数量
     */
    private Integer stock;
    /**
     * 是否热销
     */
    private Boolean isHot;
    /**
     * 是否新品
     */
    private Boolean isNew;
    /**
     * 平均评分
     */
    private BigDecimal rating;
    /**
     * 销售数量
     */
    private Integer salesCount;
    /**
     * 浏览次数
     */
    private Integer viewCount;

}
