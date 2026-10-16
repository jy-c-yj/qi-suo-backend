package com.cyj.controller;


import com.cyj.Annotation.Log;
import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dto.AdminGetCategoryByConditionDTO;
import com.cyj.dto.NewCategoryDTO;
import com.cyj.dvo.AdminGetCategoryDVO;
import com.cyj.service.CategoryManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 分类管理
 * 分类与属性这一域三档管理员都可读写
 */
@RestController
@RequestMapping("/api/admin/category")
public class CategoryManagementController {
    @Autowired
    private CategoryManagementService categoryManagementService;
    /**
     * 分页查询所有分类
     * @return
     */
    @PreAuthorize("hasAnyAuthority('SystemADMIN','SuperAdmin','OrdinaryAdmin','system','USER')")
    @GetMapping("/getCategory")
    public Result<PageResult<AdminGetCategoryDVO>> getCategory(int pageNum, int pageSize) {
            return categoryManagementService.getCategory(pageNum, pageSize);
    }
    /**
     * 条件查询
     */
    @PreAuthorize("hasAnyAuthority('SystemADMIN','SuperAdmin','OrdinaryAdmin','system','USER')")
    @PostMapping("/getCategoryByCondition")
    public Result<PageResult<AdminGetCategoryDVO>> getCategoryByCondition(@RequestBody AdminGetCategoryByConditionDTO adminGetCategoryByConditionDTO) {
            return categoryManagementService.getCategoryByCondition(adminGetCategoryByConditionDTO);
    }

    /**
     * 启用 |禁用
     */
    @Log(module = "分类", desc = "启用或禁用分类")
    @PreAuthorize("hasAnyAuthority('SystemADMIN','SuperAdmin','OrdinaryAdmin','system')")
    @PutMapping("/setCategoryEnabled")
    public Result<String> setCategoryEnabled(@RequestParam("id") Long id,@RequestParam int pageNum){
        return categoryManagementService.setCategoryEnabled(id, pageNum);
    }
    /**
     * 删除
     */
    @Log(module = "分类", desc = "删除分类")
    @PreAuthorize("hasAnyAuthority('SystemADMIN','SuperAdmin','OrdinaryAdmin','system')")
    @DeleteMapping("/deleteCategory/{id}")
    public Result<String> deleteCategory(@PathVariable("id") Integer id){
        return categoryManagementService.deleteCategory(id);
    }
    /**
     * 新增
     */
    @Log(module = "分类", desc = "新增分类")
    @PreAuthorize("hasAnyAuthority('SystemADMIN','SuperAdmin','OrdinaryAdmin','system')")
    @PostMapping("/newCategory")
    public Result<String> newCategory(@RequestBody NewCategoryDTO newCategoryDTO){
        return categoryManagementService.newCategory(newCategoryDTO);
    }
}
