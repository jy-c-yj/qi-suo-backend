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
 * 属性值表
 * </p>
 *
 * @author cyj
 * @since 2026-01-27
 */
@Getter
@Setter
@TableName("attribute_value")
public class AttributeValue implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 属性值ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 属性ID
     */
    @TableField("attribute_id")
    private Integer attributeId;

    /**
     * 属性值
     */
    @TableField("value")
    private String value;

    /**
     * 颜色代码
     */
    @TableField("color_code")
    private String colorCode;

    /**
     * 排序值
     */
    @TableField("sort_order")
    private Integer sortOrder;

    @TableField("created_at")
    private LocalDateTime createdAt;
}
