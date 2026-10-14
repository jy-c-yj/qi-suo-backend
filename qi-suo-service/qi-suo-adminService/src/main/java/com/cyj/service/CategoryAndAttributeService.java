package com.cyj.service;

import com.cyj.common.Result;
import com.cyj.dvo.AttributeDVO;
import com.cyj.dvo.AttributeValueDVO;
import com.cyj.dvo.CategoryDVO;

import java.util.List;

public interface CategoryAndAttributeService {
    Result<List<CategoryDVO>> getCategory();

    Result<List<AttributeDVO>> getAttribute();

    Result<List<AttributeValueDVO>> getAttributeValue(Integer id);
}
