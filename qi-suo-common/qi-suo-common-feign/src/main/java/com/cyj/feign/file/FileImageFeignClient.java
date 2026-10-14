package com.cyj.feign.file;


import com.cyj.back.FileImageFeignClientFallBack;
import com.cyj.common.Result;
import com.cyj.dvo.CarouselImageDVO;
import com.cyj.dvo.HomeBackgroundImageDVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
//
@FeignClient(value = "qi-suo-file",fallback = FileImageFeignClientFallBack.class)
@Primary
public interface FileImageFeignClient {
    /**
     * 查询主页轮播图片列表
     */
    @GetMapping("/api/file/image/getCarouselImageList")
    Result<List<CarouselImageDVO>> getCarouselImageList();

    /**
     * 获取登陆界面图片
     */
    @GetMapping("/api/file/image/getNowHomeBackGroundImage")
    Result<HomeBackgroundImageDVO> getNowHomeBackGroundImage();
}
