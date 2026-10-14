package com.cyj.back;


import com.cyj.common.Result;
import com.cyj.dvo.HomeCategoryDVO;
import com.cyj.feign.admin.AdminCategoryFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class AdminCategoryFeignClientFallBack implements AdminCategoryFeignClient {
    @Override
    public Result<List<HomeCategoryDVO>> getHomeCategory() {
        log.error("远程调用 -> qi-suo-admin  方法 -> getHomeCategory 出错");
        return Result.error("获取分类列表出错");
    }
}
