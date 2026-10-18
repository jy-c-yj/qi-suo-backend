package com.cyj.feign.admin;


import com.cyj.back.AdminCategoryFeignClientFallBack;
import com.cyj.common.Result;
import com.cyj.dvo.AttributeDVO;
import com.cyj.dvo.AttributeValueDVO;
import com.cyj.dvo.CategoryDVO;
import com.cyj.dvo.HomeCategoryDVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "qi-suo-admin", fallback = AdminCategoryFeignClientFallBack.class)
@Primary
public interface AdminCategoryFeignClient {
    /**
     * 查询主页分类显示列表（三层树）
     */
    @GetMapping("/api/admin/CategoryAndAttribute/getHomeCategory")
    Result<List<HomeCategoryDVO>> getHomeCategory();

    /**
     * 查询所有分类
     */
    @GetMapping("/api/admin/CategoryAndAttribute/getCategory")
    Result<List<CategoryDVO>> getCategory();

    /**
     * 查询所有属性
     */
    @GetMapping("/api/admin/CategoryAndAttribute/getAttribute")
    Result<List<AttributeDVO>> getAttribute();

    /**
     * 查询对应属性的属性值
     */
    @GetMapping("/api/admin/CategoryAndAttribute/getAttribute/value/{id}")
    Result<List<AttributeValueDVO>> getAttributeValue(@PathVariable("id") Integer id);
}
