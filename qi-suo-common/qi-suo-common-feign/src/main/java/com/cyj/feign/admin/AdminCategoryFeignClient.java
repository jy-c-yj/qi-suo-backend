package com.cyj.feign.admin;


import com.cyj.back.AdminCategoryFeignClientFallBack;
import com.cyj.common.Result;
import com.cyj.dvo.HomeCategoryDVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "qi-suo-admin", fallback = AdminCategoryFeignClientFallBack.class)
@Primary
public interface AdminCategoryFeignClient {
    /**
     * 查询主页分类显示列表（三层树）
     */
    @GetMapping("/api/admin/CategoryAndAttribute/getHomeCategory")
    Result<List<HomeCategoryDVO>> getHomeCategory();
}
