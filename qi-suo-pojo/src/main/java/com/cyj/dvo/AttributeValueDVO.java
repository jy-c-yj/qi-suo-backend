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
 * 属性值表
 * </p>
 *
 * @author cyj
 * @since 2026-01-27
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttributeValueDVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 属性值ID
     */
    private Integer id;

    /**
     * 属性ID
     */
    private Integer attributeId;

    /**
     * 属性值
     */
    private String value;

}
