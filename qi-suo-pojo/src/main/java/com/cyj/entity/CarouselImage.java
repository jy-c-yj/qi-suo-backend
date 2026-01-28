package com.cyj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 轮播图片表
 * </p>
 *
 * @author cyj
 * @since 2026-01-27
 */
@Getter
@Setter
@TableName("carousel_image")
public class CarouselImage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 唯一标识符，可用于前端展示
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
     * 是否启用：1-启用，0-禁用
     */
    @TableField("is_enabled")
    private Boolean isEnabled;

    /**
     * 是否删除：1-已删除，0-未删除
     */
    @TableField("is_deleted")
    private Boolean isDeleted;

    /**
     * 创建人
     */
    @TableField("created_by")
    private String createdBy;

    /**
     * 修改人
     */
    @TableField("modified_by")
    private String modifiedBy;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private LocalDateTime createdTime;

    /**
     * 修改时间
     */
    @TableField("modified_time")
    private LocalDateTime modifiedTime;
}
