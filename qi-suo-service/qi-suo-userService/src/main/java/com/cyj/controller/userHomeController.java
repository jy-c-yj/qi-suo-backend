package com.cyj.controller;


import com.cyj.common.Result;
import com.cyj.dvo.CarouselImageDVO;
import com.cyj.dvo.HomeBackgroundImageDVO;
import com.cyj.dvo.HomeCategoryDVO;
import com.cyj.service.CategoryService;
import com.cyj.service.HomeImageService;
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
    @Autowired
    private HomeImageService homeImageService;
    /**
     * 获取主页分类显示列表
     */
    @GetMapping("/home/category")
    public Result<List<HomeCategoryDVO>> category(){
        return categoryService.getHomeCategory();
    }
    /**
     * 获取登陆界面背景图片
     */
    @GetMapping("/home/getBackGroundImage")
    public Result<String> getBackGroundImage(){
       return  homeImageService.getBackGroundImage();
    }

    /**
     * 获取主页轮播图片
     */
    @GetMapping("/home/getCarouselImageList")
    public Result<List<String>> getCarouselImageList(){
        return  homeImageService.getCarouselImageList();
    }
}
