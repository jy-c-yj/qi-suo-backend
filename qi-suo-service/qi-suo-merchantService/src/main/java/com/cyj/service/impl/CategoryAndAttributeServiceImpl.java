package com.cyj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cyj.common.Result;
import com.cyj.dvo.AttributeDVO;
import com.cyj.dvo.AttributeValueDVO;
import com.cyj.dvo.CategoryDVO;
import com.cyj.entity.Attribute;
import com.cyj.entity.AttributeValue;
import com.cyj.entity.Category;
import com.cyj.mapper.AttributeMapper;
import com.cyj.mapper.AttributeValueMapper;
import com.cyj.mapper.CategoryMapper;
import com.cyj.service.CategoryAndAttributeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


@Service
@Slf4j
public class CategoryAndAttributeServiceImpl  implements CategoryAndAttributeService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private AttributeMapper attributeMapper;
    @Autowired
    private AttributeValueMapper attributeValueMapper;
    @Override
    public Result<List<CategoryDVO>> getCategory() {
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_active", 1).eq("is_deleted", false).orderByAsc("sort_order");
        List<Category> list = categoryMapper.selectList(queryWrapper);

        List<CategoryDVO> categoryDVOList = new LinkedList<>();
        for (Category category : list) {
            CategoryDVO categoryDVO = new CategoryDVO();
            categoryDVO.setId(category.getId());
            categoryDVO.setName(category.getName());
            categoryDVOList.add(categoryDVO);
        }
        return Result.success(categoryDVOList);
    }

    @Override
    public Result<List<AttributeDVO>> getAttribute() {
        QueryWrapper<Attribute> queryWrapper = new QueryWrapper<>();
        List<Attribute> list = attributeMapper.selectList(queryWrapper);
        List<AttributeDVO> attributeDVOList = new LinkedList<>();
        for (Attribute attribute : list) {
            AttributeDVO attributeDVO = new AttributeDVO();
            attributeDVO.setId(attribute.getId());
            attributeDVO.setAttributeType(attribute.getAttributeType());
            attributeDVO.setAttributeName(attribute.getAttributeName());
            attributeDVOList.add(attributeDVO);
        }
        return Result.success(attributeDVOList);
    }

    @Override
    public Result<List<AttributeValueDVO>> getAttributeValue(Integer id) {
        QueryWrapper<AttributeValue> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("attribute_id", id);
        List<AttributeValue> list = attributeValueMapper.selectList(queryWrapper);
        List<AttributeValueDVO> attributeValueDVOList = new LinkedList<>();
        for (AttributeValue attributeValue : list) {
            AttributeValueDVO attributeValueDVO = new AttributeValueDVO();
            attributeValueDVO.setId(attributeValue.getId());
            attributeValueDVO.setAttributeId(attributeValue.getAttributeId());
            attributeValueDVO.setValue(attributeValue.getValue());
            attributeValueDVOList.add(attributeValueDVO);

        }
        return Result.success(attributeValueDVOList);
    }
}
