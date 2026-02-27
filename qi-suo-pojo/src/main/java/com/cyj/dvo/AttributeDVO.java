package com.cyj.dvo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 属性表
 * </p>
 *
 * @author cyj
 * @since 2026-01-27
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttributeDVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 属性ID
     */
    private Integer id;

    /**
     * 属性名称
     */
    private String attributeName;


    /**
     * 属性类型：1-规格属性，2-销售属性
     */
    private Byte attributeType;

}
