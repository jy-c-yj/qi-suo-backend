package com.cyj.dvo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
@NoArgsConstructor
@AllArgsConstructor
@TableName("category")
public class HomeCategoryDVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 分类ID
     */
    private Integer id;

    /**
     * 父分类ID，0表示顶级分类
     */
    private Integer parentId;

    /**
     * 分类名称
     */
    private String name;

    /**
     *
     * 子类列表
     */
    private List<HomeCategoryDVO> children;
}
