package com.cyj.controller;


import com.cyj.common.Result;
import com.cyj.dto.CarouselImageDetailsDTO;
import com.cyj.dvo.CarouselImageDVO;
import com.cyj.service.ImageManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 图像管理
 */
@RestController
@RequestMapping("/api/admin/image")
public class imageManagementController {
    @Autowired
    private ImageManagementService imageManagementService;
    /**
     * 查询主页背景图片列表
     */
    /**
     * 设置启用
     */
    /**
     * 设置禁用
     */
    /**
     * 新增图片
     */
    /**
     * 删除
     */

    /**
     * 查询主页轮播图片列表
     */
    @GetMapping("/getCarouselImageList")
    public Result<List<CarouselImageDVO>> getCarouselImageList() {
        return imageManagementService.getCarouselImageList();
    }
    /**
     * 设置启用
     */
    @PutMapping("/setCarouselImageEnabled")
    public Result<String> setCarouselImageEnabled(@RequestParam("uid") String imageUid){
        return imageManagementService.setCarouselImageEnabled(imageUid);
    }
    /**
     * 设置禁用
     */
    @PutMapping("/setCarouselImageNotEnabled")
    public Result<String> setCarouselImageNotEnabled(@RequestParam("uid") String imageUid){
        return imageManagementService.setCarouselImageNotEnabled(imageUid);
    }
    /**
     * 新增图片
     */
    @PostMapping("/newCarouselImage")
    public Result<String> newCarouselImage(@RequestParam MultipartFile file,@RequestParam String description) {
        return imageManagementService.newCarouselImage(file,description);
    }
    /**
     * 删除
     */
    @DeleteMapping("/deleteCarouselImage/{uid}")
    public Result<String> deleteCarouselImage(@PathVariable("uid") String imageUid){
        return imageManagementService.deleteCarouselImage(imageUid);
    }
    /**
     * 编辑图片信息
     */
    @PutMapping("/updateCarouselImage")
    public Result<String> updateCarouselImage(@RequestBody CarouselImageDetailsDTO carouselImageDetailsDTO){
        return imageManagementService.updateCarouselImage(carouselImageDetailsDTO);
    }


}
