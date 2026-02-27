package com.cyj.controller;


import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dto.AdminGetCategoryByConditionDTO;
import com.cyj.dto.NewCategoryDTO;
import com.cyj.dvo.AdminGetCategoryDVO;
import com.cyj.service.CategoryManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 分类管理
 */
@RestController
@RequestMapping("/api/admin/category")
public class categoryManagementController {
    @Autowired
    private CategoryManagementService categoryManagementService;
    /**
     * 分页查询所有分类
     * @return
     */
    @GetMapping("/getCategory")
    public Result<PageResult<AdminGetCategoryDVO>> getCategory(int pageNum, int pageSize) {
            return categoryManagementService.getCategory(pageNum, pageSize);
    }
    /**
     * 条件查询
     */
    @PostMapping("/getCategoryByCondition")
    public Result<PageResult<AdminGetCategoryDVO>> getCategoryByCondition(@RequestBody AdminGetCategoryByConditionDTO adminGetCategoryByConditionDTO) {
            return categoryManagementService.getCategoryByCondition(adminGetCategoryByConditionDTO);
    }

    /**
     * 启用 |禁用
     */
    @PutMapping("/setCategoryEnabled")
    public Result<String> setCategoryEnabled(@RequestParam("id") Long id,@RequestParam int pageNum){
        return categoryManagementService.setCategoryEnabled(id, pageNum);
    }
    /**
     * 删除
     */
    @DeleteMapping("/deleteCategory/{id}")
    public Result<String> deleteCategory(@PathVariable("id") Integer id){
        return categoryManagementService.deleteCategory(id);
    }
    /**
     * 新增
     */
    @PostMapping("/newCategory")
    public Result<String> newCategory(@RequestBody NewCategoryDTO newCategoryDTO){
        return categoryManagementService.newCategory(newCategoryDTO);
    }
}
