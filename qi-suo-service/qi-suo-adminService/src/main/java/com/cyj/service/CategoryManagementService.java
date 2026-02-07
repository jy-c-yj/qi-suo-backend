package com.cyj.service;

import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dto.GetCategoryByConditionDTO;
import com.cyj.dto.NewCategoryDTO;
import com.cyj.dvo.GetCategoryDVO;

public interface CategoryManagementService {
    /**
     * 分页查询所有分类
     */
    Result<PageResult<GetCategoryDVO>> getCategory(int pageNum, int pageSize);


    /**
     * 条件查询
     */
    Result<PageResult<GetCategoryDVO>> getCategoryByCondition(GetCategoryByConditionDTO getCategoryByConditionDTO);

    /**
     * 设置状态
     * @param id
     * @param pageNum
     * @return
     */
    Result<String> setCategoryEnabled(Long id, int pageNum);

    /**
     * 删除
     * @param id
     * @return
     */
    Result<String> deleteCategory(Integer id);

    /**
     * 新增
     * @param newCategoryDTO
     * @return
     */
    Result<String> newCategory(NewCategoryDTO newCategoryDTO);
}
