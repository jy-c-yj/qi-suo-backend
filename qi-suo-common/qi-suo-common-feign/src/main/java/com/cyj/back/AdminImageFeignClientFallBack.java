package com.cyj.back;


import com.cyj.common.Result;
import com.cyj.dvo.CarouselImageDVO;
import com.cyj.dvo.HomeBackgroundImageDVO;
import com.cyj.feign.admin.AdminImageFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class AdminImageFeignClientFallBack implements AdminImageFeignClient {
    @Override
    public Result<List<CarouselImageDVO>> getCarouselImageList() {
        log.error("远程调用 -> qi-suo-admin  方法 -> getCarouselImageList 出错");
        return Result.error("获取轮播图片出错");
    }

    @Override
    public Result<HomeBackgroundImageDVO> getNowHomeBackGroundImage() {
        log.error("远程调用 -> qi-suo-admin  方法 -> getNowHomeBackGroundImage 出错");
        return Result.error("获取背景图片出错");
    }
}
