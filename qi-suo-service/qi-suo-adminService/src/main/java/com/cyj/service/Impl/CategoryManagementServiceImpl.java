package com.cyj.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.constant.RedissonKey;
import com.cyj.dto.AdminGetCategoryByConditionDTO;
import com.cyj.dto.NewCategoryDTO;
import com.cyj.dvo.AdminGetCategoryDVO;
import com.cyj.entity.Category;
import com.cyj.entity.HomeBackgroundImage;
import com.cyj.mapper.CategoryMapper;
import com.cyj.service.CategoryManagementService;
import com.cyj.utils.RedissonUtil;
import lombok.extern.slf4j.Slf4j;
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
    public Result<PageResult<AdminGetCategoryDVO>> getCategory(int pageNum, int pageSize) {
        String key = RedissonKey.CATEGORY_PAGE_KEY+pageNum;
        //查询缓存
        List<AdminGetCategoryDVO> adminGetCategoryDVOList = redissonUtil.getList(key);
        //存在返回
        if(adminGetCategoryDVOList !=null && !adminGetCategoryDVOList.isEmpty()){
            PageResult<AdminGetCategoryDVO> pageResult = new PageResult<>();
            pageResult.setTotal(Integer.toUnsignedLong(adminGetCategoryDVOList.size()) );
            pageResult.setRows(adminGetCategoryDVOList);
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
            AdminGetCategoryDVO adminGetCategoryDVO = new AdminGetCategoryDVO();
            adminGetCategoryDVO.setId(category.getId());
            adminGetCategoryDVO.setName(category.getName());
            adminGetCategoryDVO.setCategoryType(Category.setCategoryTypeToString(category.getCategoryType()));
            adminGetCategoryDVO.setIsActive(category.getIsActive());
            adminGetCategoryDVO.setLevel(category.getLevel());
            adminGetCategoryDVO.setPath(category.getPath());
            adminGetCategoryDVO.setCreatedAt(category.getCreatedAt());
            adminGetCategoryDVO.setUpdatedAt(category.getUpdatedAt());
            Category category1 = categoryMapper.selectById(category.getParentId());
            if(category1!=null){
                adminGetCategoryDVO.setParentName(category1.getName());
            }
            adminGetCategoryDVOList.add(adminGetCategoryDVO);
        }
        //返回
        PageResult<AdminGetCategoryDVO> pageResult = new PageResult<>();
        pageResult.setTotal(Integer.toUnsignedLong(adminGetCategoryDVOList.size()) );
        pageResult.setRows(adminGetCategoryDVOList);
        return Result.success(pageResult);
    }

    /**
     * 条件查询
     */
    @Override
    public Result<PageResult<AdminGetCategoryDVO>> getCategoryByCondition(AdminGetCategoryByConditionDTO adminGetCategoryByConditionDTO) {
        if(adminGetCategoryByConditionDTO ==null){
            return Result.error("数据错误--》getCategoryByCondition");
        }
        if(adminGetCategoryByConditionDTO.getPageNum() < 1 || adminGetCategoryByConditionDTO.getPageSize() < 1){
            return Result.error("页码异常");
        }
        Page<HomeBackgroundImage> page = new Page<>( adminGetCategoryByConditionDTO.getPageNum()
                , adminGetCategoryByConditionDTO.getPageSize());
        Page<AdminGetCategoryDVO> pageList = categoryMapper.getCategoryByCondition(page, Category.builder()
                        .categoryType(adminGetCategoryByConditionDTO.getCategoryType())
                        .name(adminGetCategoryByConditionDTO.getName())
                        .level(adminGetCategoryByConditionDTO.getLevel())
                        .isActive(adminGetCategoryByConditionDTO.getIsActive())
                        .build());

        PageResult<AdminGetCategoryDVO> pageResult = new PageResult<>();
        pageResult.setTotal(pageList.getTotal());
        pageResult.setRows(pageList.getRecords());
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
//        List<GetCategoryDVO> list = redissonUtil.getList(RedissonKey.CATEGORY_PAGE_KEY+pageNum);
//        for(int i = 0; i < list.size(); i++){
//            GetCategoryDVO getCategoryDVO = list.get(i);
//            if(getCategoryDVO.getId() == Integer.parseInt(id.toString())){
//                getCategoryDVO.setIsActive(active);
//                list.set(i,getCategoryDVO);
//                break;
//            }
//        }

        //更新数据库
        categoryMapper.updateById(category);
        //删除redis分页缓存
        redissonUtil.deleteByPattern(RedissonKey.CATEGORY_PAGE_KEY+"*");
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
