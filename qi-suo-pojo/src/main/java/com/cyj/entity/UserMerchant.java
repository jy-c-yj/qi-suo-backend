package com.cyj.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户商家关联表实体类
 *
 * @TableName user_merchant
 */
@Data
@TableName("user_merchant")
public class UserMerchant {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID，关联user表的id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 用户名，关联user表的username
     */
    @TableField("username")
    private String username;

    /**
     * 商家ID，关联merchant表的merchant_id
     */
    @TableField("merchant_id")
    private Long merchantId;

    /**
     * 商家名，关联merchant表的merchant_name
     */
    @TableField("merchant_name")
    private String merchantName;

    /**
     * 店铺名称，关联merchant表的shop_name
     */
    @TableField("shop_name")
    private String shopName;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 是否删除：0-否，1-是
     */
    @TableField("is_deleted")
    @TableLogic
    private Integer isDeleted;

    /**
     * 用户信息（关联查询）
     */
    @TableField(exist = false)
    private User user;

    /**
     * 商家信息（关联查询）
     */
    @TableField(exist = false)
    private Merchant merchant;
}
