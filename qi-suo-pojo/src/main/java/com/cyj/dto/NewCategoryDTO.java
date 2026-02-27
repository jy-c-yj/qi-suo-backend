package com.cyj.dto;


import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewCategoryDTO {
    /**
     * 父分类ID，0表示顶级分类
     */
    private Integer parentId;
    /**
     * 分类名称
     */
    private String name;
    /**
     * 分类类型：1-空间分类，2-风格分类，3-材质分类，4-功能分类
     */
    private Integer categoryType;
    /**
     * 分类层级
     */
    private Integer level;
}
