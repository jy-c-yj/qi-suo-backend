package com.cyj.service;

import com.cyj.common.Result;
import com.cyj.dvo.HomeCategoryDVO;

import java.util.List;

/**
 * <p>
 * 分类表 服务类
 * </p>
 *
 * @author cyj
 * @since 2026-01-27
 */
public interface CategoryService {

    /**
     * 获取主页分类显示列表
     */
    public Result<List<HomeCategoryDVO>> getHomeCategory();

}
