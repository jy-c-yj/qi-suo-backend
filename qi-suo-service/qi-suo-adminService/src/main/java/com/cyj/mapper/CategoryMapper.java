package com.cyj.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyj.dvo.AdminGetCategoryDVO;
import com.cyj.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyj.entity.HomeBackgroundImage;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 分类表 Mapper 接口
 * </p>
 *
 * @author cyj
 * @since 2026-01-27
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
    Page<AdminGetCategoryDVO> getCategoryByCondition(Page<HomeBackgroundImage> page, Category category);

}
