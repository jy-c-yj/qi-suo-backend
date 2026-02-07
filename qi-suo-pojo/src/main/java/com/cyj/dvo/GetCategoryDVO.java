package com.cyj.dvo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

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
public class GetCategoryDVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 分类ID
     */
    private Integer id;

    /**
     * 父分类
     */
    private String parentName;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类类型：1-空间分类，2-风格分类，3-材质分类，4-功能分类
     */
    private String categoryType;


    /**
     * 是否启用
     */
    private Boolean isActive;

    /**
     * 分类层级
     */
    private Byte level;

    /**
     * 分类路径，如：/1/2/3
     */
    private String path;


    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;
}
