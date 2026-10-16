package com.cyj.controller;


import com.cyj.Annotation.Log;
import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dto.CarouselImageDetailsDTO;
import com.cyj.dvo.CarouselImageDVO;
import com.cyj.dvo.HomeBackgroundImageDVO;
import com.cyj.service.ImageManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 图像管理
 * 除两个已在白名单里的读接口外，其余都是管理员三档可用的运维操作
 */
@RestController
@RequestMapping("/api/file/image")
public class ImageManagementController {
    @Autowired
    private ImageManagementService imageManagementService;
    /**
     * 查询主页背景图片列表
     */
    @PreAuthorize("hasAnyAuthority('SystemADMIN','SuperAdmin','OrdinaryAdmin')")
    @GetMapping("/getHomeBackGroundImageList")
    public Result<PageResult<HomeBackgroundImageDVO>> getHomeBackGroundImageList(int pageNum, Long pageSize) {
        return imageManagementService.getHomeBackGroundImageList(pageNum,pageSize);
    }
    /**
     * 设置启用
     */
    @Log(module = "图片", desc = "启用主页背景图")
    @PreAuthorize("hasAnyAuthority('SystemADMIN','SuperAdmin','OrdinaryAdmin')")
    @PutMapping("/setHomeBackGroundImageEnabled")
    public Result<String> setHomeBackGroundImageEnabled(@RequestParam("uid") String imageUid,@RequestParam int pageNum){
        return imageManagementService.setHomeBackGroundImageEnabled(imageUid,pageNum);
    }
    /**
     * 设置禁用
     */
    @Log(module = "图片", desc = "禁用主页背景图")
    @PreAuthorize("hasAnyAuthority('SystemADMIN','SuperAdmin','OrdinaryAdmin')")
    @PutMapping("/setHomeBackGroundImageNotEnabled")
    public Result<String> setHomeBackGroundImageNotEnabled(@RequestParam("uid") String imageUid,@RequestParam int pageNum){
        return imageManagementService.setHomeBackGroundImageNotEnabled(imageUid, pageNum);
    }
    /**
     * 新增图片
     */
    @Log(module = "图片", desc = "新增主页背景图")
    @PreAuthorize("hasAnyAuthority('SystemADMIN','SuperAdmin','OrdinaryAdmin')")
    @PostMapping("/newHomeBackGroundImage")
    public Result<String> newHomeBackGroundImage(@RequestParam MultipartFile file,@RequestParam String description) {
        return imageManagementService.newHomeBackGroundImage(file,description);
    }
    /**
     * 删除
     */
    @Log(module = "图片", desc = "删除主页背景图")
    @PreAuthorize("hasAnyAuthority('SystemADMIN','SuperAdmin','OrdinaryAdmin')")
    @DeleteMapping("/deleteHomeBackGroundImage/{uid}")
    public Result<String> deleteHomeBackGroundImage(@PathVariable("uid") String imageUid){
        return imageManagementService.deleteHomeBackGroundImage(imageUid);
    }
    /**
     * 编辑主页背景图片信息
     */
    @Log(module = "图片", desc = "修改主页背景图")
    @PreAuthorize("hasAnyAuthority('SystemADMIN','SuperAdmin','OrdinaryAdmin')")
    @PutMapping("/updateHomeBackGroundImage")
    public Result<String> updateHomeBackGroundImage(@RequestBody HomeBackgroundImageDVO homeBackgroundImageDVO,@RequestParam int pageNum){
        return imageManagementService.updateHomeBackGroundImage(homeBackgroundImageDVO,pageNum);
    }
    /**
     * 获取当前背景图片（已在白名单，游客可访问）
     */
    @GetMapping("/getNowHomeBackGroundImage")
    public Result<HomeBackgroundImageDVO> getNowHomeBackGroundImage(){
        return imageManagementService.getNowHomeBackGroundImage();
    }
    /**
     * 取消当前背景图片
     */
    @Log(module = "图片", desc = "删除当前背景图")
    @PreAuthorize("hasAnyAuthority('SystemADMIN','SuperAdmin','OrdinaryAdmin')")
    @DeleteMapping("/deleteNowHomeBackGroundImage/{uid}")
    public Result<String> deleteNowHomeBackGroundImage(@PathVariable String uid){
        return imageManagementService.deleteNowHomeBackGroundImage(uid);
    }
    /**
     *设置已启用图片为当前背景图片
     */
    @Log(module = "图片", desc = "设为当前背景图")
    @PreAuthorize("hasAnyAuthority('SystemADMIN','SuperAdmin','OrdinaryAdmin')")
    @PatchMapping("/setNowHomeBackGroundImage/{uid}")
    public Result<String> setNowHomeBackGroundImage(@PathVariable String uid){
        return imageManagementService.setNowHomeBackGroundImage(uid);
    }

//----------------------------------------------------------------------------------------------

    /**
     * 查询主页轮播图片列表（已在白名单，游客可访问）
     */
    @GetMapping("/getCarouselImageList")
    public Result<List<CarouselImageDVO>> getCarouselImageList() {
        return imageManagementService.getCarouselImageList();
    }
    /**
     * 设置启用
     */
    @Log(module = "图片", desc = "启用轮播图")
    @PreAuthorize("hasAnyAuthority('SystemADMIN','SuperAdmin','OrdinaryAdmin')")
    @PutMapping("/setCarouselImageEnabled")
    public Result<String> setCarouselImageEnabled(@RequestParam("uid") String imageUid){
        return imageManagementService.setCarouselImageEnabled(imageUid);
    }
    /**
     * 设置禁用
     */
    @Log(module = "图片", desc = "禁用轮播图")
    @PreAuthorize("hasAnyAuthority('SystemADMIN','SuperAdmin','OrdinaryAdmin')")
    @PutMapping("/setCarouselImageNotEnabled")
    public Result<String> setCarouselImageNotEnabled(@RequestParam("uid") String imageUid){
        return imageManagementService.setCarouselImageNotEnabled(imageUid);
    }
    /**
     * 新增图片
     */
    @Log(module = "图片", desc = "新增轮播图")
    @PreAuthorize("hasAnyAuthority('SystemADMIN','SuperAdmin','OrdinaryAdmin')")
    @PostMapping("/newCarouselImage")
    public Result<String> newCarouselImage(@RequestParam MultipartFile file,@RequestParam String description) {
        return imageManagementService.newCarouselImage(file,description);
    }
    /**
     * 删除
     */
    @Log(module = "图片", desc = "删除轮播图")
    @PreAuthorize("hasAnyAuthority('SystemADMIN','SuperAdmin','OrdinaryAdmin')")
    @DeleteMapping("/deleteCarouselImage/{uid}")
    public Result<String> deleteCarouselImage(@PathVariable("uid") String imageUid){
        return imageManagementService.deleteCarouselImage(imageUid);
    }
    /**
     * 编辑轮播图片信息
     */
    @Log(module = "图片", desc = "修改轮播图")
    @PreAuthorize("hasAnyAuthority('SystemADMIN','SuperAdmin','OrdinaryAdmin')")
    @PutMapping("/updateCarouselImage")
    public Result<String> updateCarouselImage(@RequestBody CarouselImageDetailsDTO carouselImageDetailsDTO) {
        return imageManagementService.updateCarouselImage(carouselImageDetailsDTO);
    }
}
