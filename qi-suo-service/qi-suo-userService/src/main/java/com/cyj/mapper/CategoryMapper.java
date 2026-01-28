package com.cyj.mapper;

import com.cyj.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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

}
