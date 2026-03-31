package com.cyj.dto;


import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryProductByConditionDTO {
    /**
     *商品名
     */
    private String productName;
    /**
     * 商品分类
     */
    private Integer categoryIdValue;
    /**
     * 所属商家
     */
    private String merchantName;
    /**
     * 是否热销
     */
    private Boolean isHot;
    /**
     * 是否新品
     */
    private Boolean isNew;
    /**
     * 销量排序
     */
    private Boolean salesCountSort;
    /**
     * 价格排序
     */
    private Boolean priceSort;
    /**
     * 浏览量排序
     */
    private Boolean viewCountSort;
    /**
     * 页码
     */
    private Integer pageNum;
    /**
     * 每页数量
     */
    private Integer pageSize;
}
