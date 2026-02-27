package com.cyj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.*;

/**
 * <p>
 * 分类表
 * </p>
 *
 * @author cyj
 * @since 2026-01-27
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("category")
public class Category implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 分类ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 父分类ID，0表示顶级分类
     */
    @TableField("parent_id")
    private Integer parentId;

    /**
     * 分类名称
     */
    @TableField("name")
    private String name;

    /**
     * 分类类型：1-空间分类，2-风格分类，3-材质分类，4-功能分类
     */
    @TableField("category_type")
    private Integer categoryType;

    /**
     * 排序值，越小越靠前
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 是否启用
     */
    @TableField("is_active")
    private Boolean isActive;

    /**
     * 分类层级
     */
    @TableField("level")
    private Integer level;

    /**
     * 分类路径，如：/1/2/3
     */
    @TableField("path")
    private String path;

    /**
     * 是否删除：0-未删除，1-已删除
     */
    @TableField("is_deleted")
    private Boolean isDeleted;


    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;

    public static  String setCategoryTypeToString(int i) {
        //分类类型：1-空间分类，2-风格分类，3-材质分类，4-功能分类
        return switch (i) {
            case 1 -> "空间分类";
            case 2 -> "风格分类";
            case 3 -> "材质分类";
            case 4 -> "功能分类";
            default -> "不存在的分类";
        };
    }
}
