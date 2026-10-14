package com.cyj.service;

import com.cyj.common.Result;
import com.cyj.dvo.AttributeDVO;
import com.cyj.dvo.AttributeValueDVO;
import com.cyj.dvo.CategoryDVO;
import com.cyj.dvo.HomeCategoryDVO;

import java.util.List;

public interface CategoryAndAttributeService {
    Result<List<CategoryDVO>> getCategory();

    /**
     * 获取主页分类显示列表（三层树）
     */
    Result<List<HomeCategoryDVO>> getHomeCategory();

    Result<List<AttributeDVO>> getAttribute();

    Result<List<AttributeValueDVO>> getAttributeValue(Integer id);
}
