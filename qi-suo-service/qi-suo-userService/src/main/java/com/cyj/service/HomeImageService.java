package com.cyj.service;


import com.cyj.common.Result;
import com.cyj.dvo.CarouselImageDVO;
import com.cyj.dvo.HomeBackgroundImageDVO;

import java.util.List;

public interface HomeImageService {
    Result<String> getBackGroundImage();

    Result<List<String>> getCarouselImageList();
}
