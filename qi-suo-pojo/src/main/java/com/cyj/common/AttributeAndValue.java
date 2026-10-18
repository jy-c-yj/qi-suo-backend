package com.cyj.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttributeAndValue {
    private String attributeName;
    private String attributeValue;
    /**
     * 属性值ID（用于前端按 ID 增删）
     */
    private Integer attributeValueId;
}
