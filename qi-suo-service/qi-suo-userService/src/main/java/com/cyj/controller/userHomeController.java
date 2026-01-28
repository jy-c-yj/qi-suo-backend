package com.cyj.controller;


import com.cyj.common.Result;
import com.cyj.dvo.HomeCategoryDVO;
import com.cyj.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 主页相关
 */
@RestController
@RequestMapping("/api/user")
public class userHomeController {
    @Autowired
    private CategoryService categoryService;
    /**
     * 获取主页分类显示列表
     */
    @GetMapping("/home/category")
    public Result<List<HomeCategoryDVO>> category(){
        return categoryService.getHomeCategory();
    }
}
