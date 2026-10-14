package com.cyj.feign.admin;


import com.cyj.back.AdminImageFeignClientFallBack;
import com.cyj.common.Result;
import com.cyj.dvo.CarouselImageDVO;
import com.cyj.dvo.HomeBackgroundImageDVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
//
@FeignClient(value = "qi-suo-file",fallback = AdminImageFeignClientFallBack.class)
@Primary
public interface AdminImageFeignClient {
    /**
     * 查询主页轮播图片列表
     */
    @GetMapping("/api/admin/image/getCarouselImageList")
    Result<List<CarouselImageDVO>> getCarouselImageList();

    /**
     * 获取登陆界面图片
     */
    @GetMapping("/api/admin/image/getNowHomeBackGroundImage")
    Result<HomeBackgroundImageDVO> getNowHomeBackGroundImage();
}
