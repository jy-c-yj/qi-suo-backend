package com.cyj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 首页背景图片表
 * </p>
 *
 * @author cyj
 * @since 2026-01-27
 */
@Data
@TableName("home_background_image")
public class HomeBackgroundImage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID/唯一标识
     */
    @TableField("uid")
    private String uid;

    /**
     * 图片描述
     */
    @TableField("description")
    private String description;

    /**
     * 图片URL地址
     */
    @TableField("url")
    private String url;

    /**
     * 是否启用：0-禁用，1-启用
     */
    @TableField("is_enabled")
    private Boolean isEnabled;

    /**
     * 是否删除：0-未删除，1-已删除
     */
    @TableField("is_deleted")
    private Boolean isDeleted;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;

    /**
     * 更新时间
     */
    @TableField("updated_time")
    private Date updatedTime;
}
