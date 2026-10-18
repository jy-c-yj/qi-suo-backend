package com.cyj.back;


import com.cyj.common.Result;
import com.cyj.dvo.AttributeDVO;
import com.cyj.dvo.AttributeValueDVO;
import com.cyj.dvo.CategoryDVO;
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

    @Override
    public Result<List<CategoryDVO>> getCategory() {
        log.error("远程调用 -> qi-suo-admin  方法 -> getCategory 出错");
        return Result.error("获取分类列表出错");
    }

    @Override
    public Result<List<AttributeDVO>> getAttribute() {
        log.error("远程调用 -> qi-suo-admin  方法 -> getAttribute 出错");
        return Result.error("获取属性列表出错");
    }

    @Override
    public Result<List<AttributeValueDVO>> getAttributeValue(Integer id) {
        log.error("远程调用 -> qi-suo-admin  方法 -> getAttributeValue 出错");
        return Result.error("获取属性值列表出错");
    }
}
