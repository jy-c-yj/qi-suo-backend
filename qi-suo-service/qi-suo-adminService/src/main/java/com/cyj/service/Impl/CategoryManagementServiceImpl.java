package com.cyj.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.constant.RedissonKey;
import com.cyj.dto.GetCategoryByConditionDTO;
import com.cyj.dto.NewCategoryDTO;
import com.cyj.dvo.GetCategoryDVO;
import com.cyj.entity.Category;
import com.cyj.mapper.CategoryMapper;
import com.cyj.service.CategoryManagementService;
import com.cyj.utils.RedissonUtil;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.LifecycleState;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@Slf4j
public class CategoryManagementServiceImpl implements CategoryManagementService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private RedissonUtil redissonUtil;

    /**
     * 分页查询所有分类
     */
    @Override
    public Result<PageResult<GetCategoryDVO>> getCategory(int pageNum, int pageSize) {
        String key = RedissonKey.CATEGORY_PAGE_KEY+pageNum;
        //查询缓存
        List<GetCategoryDVO> getCategoryDVOList = redissonUtil.getList(key);
        //存在返回
        if(getCategoryDVOList!=null|| !getCategoryDVOList.isEmpty()){
            PageResult<GetCategoryDVO> pageResult = new PageResult<>();
            pageResult.setTotal(Integer.toUnsignedLong(getCategoryDVOList.size()) );
            pageResult.setRows(getCategoryDVOList);
            return Result.success(pageResult);
        }
        //不存在查询数据库
        Page<Category> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", 0);
        queryWrapper.orderByAsc("sort_order");
        Page<Category> page1 = categoryMapper.selectPage(page, queryWrapper);
        List<Category> list =page1.getRecords();
        if(list==null || list.isEmpty()){
            return Result.error("数据库查询出错，数据不存在");
        }
        //增加缓存
        for(Category category:list){
            GetCategoryDVO getCategoryDVO = new GetCategoryDVO();
            getCategoryDVO.setId(category.getId());
            getCategoryDVO.setName(category.getName());
            getCategoryDVO.setCategoryType(Category.setCategoryType(category.getCategoryType()));
            getCategoryDVO.setIsActive(category.getIsActive());
            getCategoryDVO.setLevel(category.getLevel());
            getCategoryDVO.setPath(category.getPath());
            getCategoryDVO.setCreatedAt(category.getCreatedAt());
            getCategoryDVO.setUpdatedAt(category.getUpdatedAt());
            Category category1 = categoryMapper.selectById(category.getParentId());
            if(category1!=null){
                getCategoryDVO.setParentName(category1.getName());
            }
            getCategoryDVOList.add(getCategoryDVO);
        }
        //返回
        PageResult<GetCategoryDVO> pageResult = new PageResult<>();
        pageResult.setTotal(Integer.toUnsignedLong(getCategoryDVOList.size()) );
        pageResult.setRows(getCategoryDVOList);
        return Result.success(pageResult);
    }

    /**
     * 条件查询
     */
    @Override
    public Result<PageResult<GetCategoryDVO>> getCategoryByCondition(GetCategoryByConditionDTO getCategoryByConditionDTO) {
        if(getCategoryByConditionDTO==null){
            return Result.error("数据错误--》getCategoryByCondition");
        }
        if(getCategoryByConditionDTO.getPageNum() < 1 || getCategoryByConditionDTO.getPageSize() < 1){
            return Result.error("页码异常");
        }
        PageHelper.startPage(getCategoryByConditionDTO.getPageNum(), getCategoryByConditionDTO.getPageSize());
        com.github.pagehelper.Page<GetCategoryDVO> page = categoryMapper.getCategoryByCondition(Category.builder()
                        .categoryType(getCategoryByConditionDTO.getCategoryType())
                        .name(getCategoryByConditionDTO.getName())
                        .level(getCategoryByConditionDTO.getLevel())
                        .isActive(getCategoryByConditionDTO.getIsActive())
                .build());
        List<GetCategoryDVO> list = page.getResult();
        PageResult<GetCategoryDVO> pageResult = new PageResult<>();
        pageResult.setTotal(page.getTotal());
        pageResult.setRows(list);
        return Result.success(pageResult) ;
    }

    /**
     * 设置状态
     * @param id
     * @param pageNum
     * @return
     */
    @Override
    public Result<String> setCategoryEnabled(Long id, int pageNum) {
        //查询数据库
        Category category = categoryMapper.selectById(id);
        if(category==null){
            log.error("查询数据库为空setCategoryEnabled -> id : {}" , id);
            return Result.error("查询数据库为空 id : " + id);
        }
        //更新状态
        Boolean active = category.getIsActive();
        if(active){
            category.setIsActive(false);
            active = false;
        }else {
            category.setIsActive(true);
            active = true;
        }
        //更新缓存
        List<GetCategoryDVO> list = redissonUtil.getList(RedissonKey.CATEGORY_PAGE_KEY+pageNum);
        for(int i = 0; i < list.size(); i++){
            GetCategoryDVO getCategoryDVO = list.get(i);
            if(getCategoryDVO.getId().equals(id)){
                getCategoryDVO.setIsActive(active);
                list.set(i,getCategoryDVO);
                break;
            }
        }
        return Result.success("设置成功");
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @Override
    public Result<String> deleteCategory(Integer id) {
        //数据库逻辑删除
        Category category = new Category();
        category.setId(id);
        category.setIsDeleted(true);
        categoryMapper.updateById(category);
        //删除redis分页缓存
        redissonUtil.deleteByPattern(RedissonKey.CATEGORY_PAGE_KEY+"*");
        return Result.success();
    }
    /**
     * 新增
     * @param newCategoryDTO
     * @return
     */
    @Override
    public Result<String> newCategory(NewCategoryDTO newCategoryDTO) {
        //判断数据完整性
        if(newCategoryDTO == null){
            log.error("数据错误 --> newCategoryDTO == null");
            Result.error("数据错误");
        }
        if (newCategoryDTO.getCategoryType() == null ||
                newCategoryDTO.getName() == null ||
                newCategoryDTO.getLevel() == null ||
                newCategoryDTO.getName().equals(" ") ) {
            log.error("数据错误");
            return Result.error("数据错误");
        }
        //数据库新增
        Category category = Category.builder()
                .sortOrder(10)
                .isActive(false)
                .isDeleted(false)
                .updatedAt(null)
                .createdAt(LocalDateTime.now())
                .level(newCategoryDTO.getLevel())
                .name(newCategoryDTO.getName())
                .parentId(newCategoryDTO.getParentId())
                .categoryType(newCategoryDTO.getCategoryType())
                .path("/")
                .build();
        categoryMapper.insert(category);
        if(category.getLevel() == 1){
            category.setPath("/"+category.getId());
        }else {
            category.setPath("/"+category.getParentId()+"/"+category.getId());
        }
        categoryMapper.updateById(category);
        //更新缓存
        redissonUtil.deleteByPattern(RedissonKey.CATEGORY_PAGE_KEY+"*");
        return Result.success();
    }
}
