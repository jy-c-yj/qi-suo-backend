package com.cyj.mapper;

import com.cyj.dvo.GetCategoryDVO;
import com.cyj.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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
    Page<GetCategoryDVO> getCategoryByCondition(Category category);

}
