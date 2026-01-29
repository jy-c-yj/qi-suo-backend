package com.cyj.controller;


import com.cyj.common.Result;
import com.cyj.dto.CarouselImageDetailsDTO;
import com.cyj.dvo.CarouselImageDVO;
import com.cyj.dvo.HomeBackgroundImageDVO;
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
    @GetMapping("/getHomeBackGroundImageList")
    private Result<List<HomeBackgroundImageDVO>> getHomeBackGroundImageList() {
        return imageManagementService.getHomeBackGroundImageList();
    }
    /**
     * 设置启用
     */
    @PutMapping("/setHomeBackGroundImageEnabled")
    public Result<String> setHomeBackGroundImageEnabled(@RequestParam("uid") String imageUid){
        return imageManagementService.setHomeBackGroundImageEnabled(imageUid);
    }
    /**
     * 设置禁用
     */
    @PutMapping("/setHomeBackGroundImageNotEnabled")
    public Result<String> setHomeBackGroundImageNotEnabled(@RequestParam("uid") String imageUid){
        return imageManagementService.setHomeBackGroundImageNotEnabled(imageUid);
    }
    /**
     * 新增图片
     */
    @PostMapping("/newHomeBackGroundImage")
    public Result<String> newHomeBackGroundImage(@RequestParam MultipartFile file,@RequestParam String description) {
        return imageManagementService.newHomeBackGroundImage(file,description);
    }
    /**
     * 删除
     */
    @DeleteMapping("/deleteHomeBackGroundImage/{uid}")
    public Result<String> deleteHomeBackGroundImage(@PathVariable("uid") String imageUid){
        return imageManagementService.deleteHomeBackGroundImage(imageUid);
    }
    /**
     * 编辑主页背景图片信息
     */
    @PutMapping("/updateHomeBackGroundImage")
    public Result<String> updateHomeBackGroundImage(@RequestBody HomeBackgroundImageDVO homeBackgroundImageDVO){
        return imageManagementService.updateHomeBackGroundImage(homeBackgroundImageDVO);
    }
    /**
     * 获取当前背景图片
     */
    @GetMapping("/getNowHomeBackGroundImage")
    public Result<HomeBackgroundImageDVO> getNowHomeBackGroundImage(){
        return imageManagementService.getNowHomeBackGroundImage();
    }
    /**
     * 取消当前背景图片
     */
    @DeleteMapping("/deleteNowHomeBackGroundImage/{uid}")
    public Result<String> deleteNowHomeBackGroundImage(@PathVariable String uid){
        return imageManagementService.deleteNowHomeBackGroundImage(uid);
    }
    /**
     *设置已启用图片为当前背景图片
     */
    @PatchMapping("/setNowHomeBackGroundImage/{uid}")
    public Result<String> setNowHomeBackGroundImage(@PathVariable String uid){
        return imageManagementService.setNowHomeBackGroundImage(uid);
    }

//----------------------------------------------------------------------------------------------

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
     * 编辑轮播图片信息
     */
    @PutMapping("/updateCarouselImage")
    public Result<String> updateCarouselImage(@RequestBody CarouselImageDetailsDTO carouselImageDetailsDTO) {
        return imageManagementService.updateCarouselImage(carouselImageDetailsDTO);
    }
}