package com.cyj.service;

import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dto.AdminGetCategoryByConditionDTO;
import com.cyj.dto.NewCategoryDTO;
import com.cyj.dvo.AdminGetCategoryDVO;

public interface CategoryManagementService {
    /**
     * 分页查询所有分类
     */
    Result<PageResult<AdminGetCategoryDVO>> getCategory(int pageNum, int pageSize);


    /**
     * 条件查询
     */
    Result<PageResult<AdminGetCategoryDVO>> getCategoryByCondition(AdminGetCategoryByConditionDTO adminGetCategoryByConditionDTO);

    /**
     * 设置状态
     * @param id
     * @param pageNum
     * @return
     */
    Result<String> setCategoryEnabled(Long id, int pageNum);

    /**
     * 显式设为禁用
     */
    Result<String> setCategoryNotEnabled(Long id, int pageNum);

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
