package com.cyj.service.impl;


import com.cyj.common.Result;
import com.cyj.dvo.CarouselImageDVO;
import com.cyj.dvo.HomeBackgroundImageDVO;
import com.cyj.feign.admin.AdminImageFeignClient;
import com.cyj.service.HomeImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class HomeImageServiceImpl implements HomeImageService {
    @Autowired
    private AdminImageFeignClient adminImageFeignClient;
    @Override
    public Result<String> getBackGroundImage() {
        Result<HomeBackgroundImageDVO> result = adminImageFeignClient.getNowHomeBackGroundImage();

        if(result.getCode() == 0){
            return Result.error(result.getMsg());
        }

        if(result.getCode() == 1 && result.getData() == null){
            return Result.error(result.getMsg());
        }

        return Result.success(result.getData().getUrl());
    }

    @Override
    public Result<List<String>> getCarouselImageList() {
        Result<List<CarouselImageDVO>> result = adminImageFeignClient.getCarouselImageList();

        if(result.getCode() == 0){
            return Result.error(result.getMsg());
        }

        if(result.getCode() == 1 && result.getData() == null){
            return Result.error("获取轮播图片列表为空");
        }
        List<String> list = result.getData().stream().map(CarouselImageDVO::getUrl).toList();

        return Result.success(list);
    }
}
