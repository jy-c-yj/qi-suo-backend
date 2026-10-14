package com.cyj.service.impl;

import com.cyj.common.Result;
import com.cyj.dvo.HomeCategoryDVO;
import com.cyj.feign.admin.AdminCategoryFeignClient;
import com.cyj.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 分类表 服务实现类
 * </p>
 *
 * @author cyj
 * @since 2026-01-27
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private AdminCategoryFeignClient adminCategoryFeignClient;

    @Override
    public Result<List<HomeCategoryDVO>> getHomeCategory() {
        Result<List<HomeCategoryDVO>> result = adminCategoryFeignClient.getHomeCategory();

        if(result.getCode() == 0){
            return Result.error(result.getMsg());
        }

        return Result.success(result.getData());
    }
}
