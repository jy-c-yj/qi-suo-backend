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
 * 属性表
 * </p>
 *
 * @author cyj
 * @since 2026-01-27
 */
@Getter
@Setter
@TableName("attribute")
public class Attribute implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 属性ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 属性名称
     */
    @TableField("attribute_name")
    private String attributeName;


    /**
     * 属性类型：1-规格属性，2-销售属性
     */
    @TableField("attribute_type")
    private Byte attributeType;



    @TableField("created_at")
    private LocalDateTime createdAt;
}
