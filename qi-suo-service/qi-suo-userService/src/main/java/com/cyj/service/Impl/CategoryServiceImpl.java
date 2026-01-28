package com.cyj.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cyj.common.Result;
import com.cyj.dvo.HomeCategoryDVO;
import com.cyj.entity.Category;
import com.cyj.mapper.CategoryMapper;
import com.cyj.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;


    @Override
    public Result<List<HomeCategoryDVO>> getHomeCategory() {
        //获取所有分类列表
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.eq("is_active",1);
        List<Category> categoryList = categoryMapper.selectList(wrapper);

        List<HomeCategoryDVO> homeCategoryDVOList = new ArrayList<>();

        //获取所有顶层分类
        categoryList.forEach(category -> {
            if(category.getIsActive() && category.getLevel() == 1){
                homeCategoryDVOList.add(HomeCategoryDVO.builder()
                                .id(category.getId())
                                .parentId(category.getParentId())
                                .name(category.getName())
                                .children(new ArrayList<>())
                        .build());
            }
        });


        List<HomeCategoryDVO> homeCategoryDVOList3 = new ArrayList<>();
        //获取所有三级分类
        categoryList.forEach(category -> {
            if(category.getIsActive() && category.getLevel() == 3){
                homeCategoryDVOList3.add(HomeCategoryDVO.builder()
                        .id(category.getId())
                        .parentId(category.getParentId())
                        .name(category.getName())
                        .children(new ArrayList<>())
                        .build());
            }
        });

        List<HomeCategoryDVO> homeCategoryDVOList2 = new ArrayList<>();
        //获取所有二级分类
        categoryList.forEach(category -> {
            if(category.getIsActive() && category.getLevel() == 2){
                homeCategoryDVOList2.add(HomeCategoryDVO.builder()
                        .id(category.getId())
                        .parentId(category.getParentId())
                        .name(category.getName())
                        .children(new ArrayList<>())
                        .build());
            }
        });

        //为二级分类分配子分类
        if (!homeCategoryDVOList3.isEmpty()) {
            homeCategoryDVOList3.forEach(homeCategoryDVO -> {
                homeCategoryDVOList2.forEach(homeCategoryDVO1 -> {
                    if (homeCategoryDVO.getParentId().equals(homeCategoryDVO1.getId())) {
                        homeCategoryDVO1.getChildren().add(homeCategoryDVO);
                    }
                });
            });
        }
        //为顶级分类分配子分类
        if (!homeCategoryDVOList2.isEmpty()) {
            homeCategoryDVOList2.forEach(homeCategoryDVO -> {
                homeCategoryDVOList.forEach(homeCategoryDVO1 -> {
                    if (homeCategoryDVO.getParentId().equals(homeCategoryDVO1.getId())) {
                        homeCategoryDVO1.getChildren().add(homeCategoryDVO);
                    }
                });
            });
        }
        return Result.success(homeCategoryDVOList);
    }
}
