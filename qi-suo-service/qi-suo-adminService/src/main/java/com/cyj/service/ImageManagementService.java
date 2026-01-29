package com.cyj.service;

import com.cyj.common.Result;
import com.cyj.dto.CarouselImageDetailsDTO;
import com.cyj.dvo.CarouselImageDVO;
import com.cyj.dvo.HomeBackgroundImageDVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageManagementService {


    /**
     * 主页轮播图像管理
     */
    Result<List<CarouselImageDVO>> getCarouselImageList();

    /**
     * 新增轮播图片
     *
     * @param file
     * @param description
     * @return
     */
    Result<String> newCarouselImage(MultipartFile file, String description);

    /**
     * 轮播图片设为启用
     *
     * @param imageUid
     * @return
     */
    Result<String> setCarouselImageEnabled(String imageUid);

    /**
     * 轮播图片设为禁用
     *
     * @param imageUid
     * @return
     */
    Result<String> setCarouselImageNotEnabled(String imageUid);

    /**
     * 删除图片
     *
     * @param imageUid
     * @return
     */
    Result<String> deleteCarouselImage(String imageUid);

    /**
     * 修改图片信息
     *
     * @param carouselImageDetailsDTO
     * @return
     */
    Result<String> updateCarouselImage(CarouselImageDetailsDTO carouselImageDetailsDTO);

    /**
     * 主页背景图像管理
     */
    Result<List<HomeBackgroundImageDVO>> getHomeBackGroundImageList();

    /**
     * 设置启用
     *
     * @param imageUid
     * @return
     */
    Result<String> setHomeBackGroundImageEnabled(String imageUid);

    /**
     * 设置禁用
     *
     * @param imageUid
     * @return
     */
    Result<String> setHomeBackGroundImageNotEnabled(String imageUid);

    /**
     * 新增
     *
     * @param file
     * @param description
     * @return
     */
    Result<String> newHomeBackGroundImage(MultipartFile file, String description);

    /**
     * 删除
     * @param imageUid
     * @return
     */
    Result<String> deleteHomeBackGroundImage(String imageUid);

    /**
     * 编辑
     * @param homeBackgroundImageDVO
     * @return
     */
    Result<String> updateHomeBackGroundImage(HomeBackgroundImageDVO homeBackgroundImageDVO);

    /**
     * 获取当前背景图片
     * @return
     */
    Result<HomeBackgroundImageDVO> getNowHomeBackGroundImage();

    /**
     * 取消当前背景图片
     */
    Result<String> deleteNowHomeBackGroundImage(String uid);

    /**
     *设置已启用图片为当前背景图片
     */
    Result<String> setNowHomeBackGroundImage(String uid);
}

