package com.cyj.service;

import com.cyj.common.Result;
import com.cyj.dvo.HomeCategoryDVO;
import com.cyj.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 分类表 服务类
 * </p>
 *
 * @author cyj
 * @since 2026-01-27
 */
public interface CategoryService extends IService<Category> {

    /**
     * 获取主页分类显示列表
     */
    public Result<List<HomeCategoryDVO>> getHomeCategory();

}
