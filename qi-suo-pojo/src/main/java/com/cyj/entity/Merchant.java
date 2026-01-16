package com.cyj.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 商家表 实体类
 *
 * @author 系统生成
 * @since 2024-12-25
 */
@Data
@TableName("merchant")
public class Merchant implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 商家ID
     */
    @TableId(value = "merchant_id", type = IdType.AUTO)
    private Long merchantId;

    /**
     * 商家名
     */
    @TableField(value = "merchant_name")
    private String merchantName;

    /**
     * 商家密码
     */
    @TableField(value = "merchant_password")
    private String merchantPassword;

    /**
     * 店铺名称
     */
    @TableField(value = "shop_name")
    private String shopName;

    /**
     * 店铺Logo
     */
    @TableField(value = "shop_logo_url")
    private String shopLogoUrl;

    /**
     * 店铺描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 联系人姓名
     */
    @TableField(value = "contact_name")
    private String contactName;

    /**
     * 联系电话
     */
    @TableField(value = "contact_phone")
    private String contactPhone;

    /**
     * 联系邮箱
     */
    @TableField(value = "contact_email")
    private String contactEmail;

    /**
     * 商家状态：0-待审核，1-正常，2-关闭，3-禁用
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField("create_by")
    private Long createBy;

    /**
     * 更新人
     */
    @TableField("update_by")
    private Long updateBy;

    /**
     * 是否删除：0-否，1-是
     */
    @TableField("is_deleted")
    @TableLogic
    private Integer isDeleted;
}