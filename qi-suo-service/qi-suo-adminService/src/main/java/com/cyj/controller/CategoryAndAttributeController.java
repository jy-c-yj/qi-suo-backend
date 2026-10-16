package com.cyj.controller;


import com.cyj.common.Result;
import com.cyj.dvo.AttributeDVO;
import com.cyj.dvo.AttributeValueDVO;
import com.cyj.dvo.CategoryDVO;
import com.cyj.dvo.HomeCategoryDVO;
import com.cyj.service.CategoryAndAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/CategoryAndAttribute")
public class CategoryAndAttributeController {

    @Autowired
    private CategoryAndAttributeService categoryAndAttributeService;

    /**
     * 查询所有分类
     */
    @PreAuthorize("hasAnyAuthority('SystemADMIN','SuperAdmin','OrdinaryAdmin','system')")
    @GetMapping("/getCategory")
    public Result<List<CategoryDVO>> getCategory() {
        return categoryAndAttributeService.getCategory();
    }

    /**
     * 查询主页分类树（供用户服务经 Feign 调用）
     */
    @PreAuthorize("hasAnyAuthority('SystemADMIN','SuperAdmin','OrdinaryAdmin','system')")
    @GetMapping("/getHomeCategory")
    public Result<List<HomeCategoryDVO>> getHomeCategory() {
        return categoryAndAttributeService.getHomeCategory();
    }

    /**
     * 查询所有属性
     */
    @PreAuthorize("hasAnyAuthority('SystemADMIN','SuperAdmin','OrdinaryAdmin','system')")
    @GetMapping("/getAttribute")
    public Result<List<AttributeDVO>> getAttribute() {
        return categoryAndAttributeService.getAttribute();
    }

    /**
     * 查询对应属性的属性值
     */
    @PreAuthorize("hasAnyAuthority('SystemADMIN','SuperAdmin','OrdinaryAdmin','system')")
    @GetMapping("/getAttribute/value/{id}")
    public Result<List<AttributeValueDVO>> getAttributeValue(@PathVariable Integer id) {
        return categoryAndAttributeService.getAttributeValue(id);
    }
}
