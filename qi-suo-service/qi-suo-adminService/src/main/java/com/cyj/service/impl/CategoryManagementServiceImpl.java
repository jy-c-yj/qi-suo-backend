package com.cyj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.constant.RedissonKey;
import com.cyj.dto.AdminGetCategoryByConditionDTO;
import com.cyj.dto.NewCategoryDTO;
import com.cyj.dvo.AdminGetCategoryDVO;
import com.cyj.entity.Category;
import com.cyj.mapper.CategoryMapper;
import com.cyj.service.CategoryManagementService;
import com.cyj.utils.RedissonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@Service
@Slf4j
public class CategoryManagementServiceImpl implements CategoryManagementService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private RedissonUtil redissonUtil;

    /**
     * 分页查询所有分类：直接查库，total 为数据库总数
     */
    @Override
    public Result<PageResult<AdminGetCategoryDVO>> getCategory(int pageNum, int pageSize) {
        if (pageNum < 1 || pageSize < 1) {
            return Result.error("页码异常");
        }
        Page<Category> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", false);
        queryWrapper.orderByAsc("sort_order");
        queryWrapper.orderByDesc("created_at");
        Page<Category> categoryPage = categoryMapper.selectPage(page, queryWrapper);

        List<AdminGetCategoryDVO> rows = categoryPage.getRecords().stream()
                .map(this::toAdminGetCategoryDVO)
                .toList();

        PageResult<AdminGetCategoryDVO> pageResult = new PageResult<>();
        pageResult.setTotal(categoryPage.getTotal());
        pageResult.setRows(rows);
        return Result.success(pageResult);
    }

    private AdminGetCategoryDVO toAdminGetCategoryDVO(Category category) {
        AdminGetCategoryDVO dvo = new AdminGetCategoryDVO();
        dvo.setId(category.getId());
        dvo.setName(category.getName());
        dvo.setCategoryType(Category.setCategoryTypeToString(category.getCategoryType()));
        dvo.setIsActive(category.getIsActive());
        dvo.setLevel(category.getLevel());
        dvo.setPath(category.getPath());
        dvo.setCreatedAt(category.getCreatedAt());
        dvo.setUpdatedAt(category.getUpdatedAt());
        if (category.getParentId() != null && category.getParentId() != 0) {
            Category parent = categoryMapper.selectById(category.getParentId());
            if (parent != null) {
                dvo.setParentName(parent.getName());
            }
        }
        return dvo;
    }

    /**
     * 条件查询
     */
    @Override
    public Result<PageResult<AdminGetCategoryDVO>> getCategoryByCondition(AdminGetCategoryByConditionDTO adminGetCategoryByConditionDTO) {
        if (adminGetCategoryByConditionDTO == null) {
            return Result.error("数据错误--》getCategoryByCondition");
        }
        if (adminGetCategoryByConditionDTO.getPageNum() < 1 || adminGetCategoryByConditionDTO.getPageSize() < 1) {
            return Result.error("页码异常");
        }
        Page<AdminGetCategoryDVO> page = new Page<>(adminGetCategoryByConditionDTO.getPageNum(),
                adminGetCategoryByConditionDTO.getPageSize());
        Page<AdminGetCategoryDVO> pageList = categoryMapper.getCategoryByCondition(page, Category.builder()
                .categoryType(adminGetCategoryByConditionDTO.getCategoryType())
                .name(adminGetCategoryByConditionDTO.getName())
                .level(adminGetCategoryByConditionDTO.getLevel())
                .isActive(adminGetCategoryByConditionDTO.getIsActive())
                .build());

        PageResult<AdminGetCategoryDVO> pageResult = new PageResult<>();
        pageResult.setTotal(pageList.getTotal());
        pageResult.setRows(pageList.getRecords());
        return Result.success(pageResult);
    }

    /**
     * 显式启用
     */
    @Override
    public Result<String> setCategoryEnabled(Long id, int pageNum) {
        return setCategoryStatus(id, true);
    }

    /**
     * 显式禁用
     */
    @Override
    public Result<String> setCategoryNotEnabled(Long id, int pageNum) {
        return setCategoryStatus(id, false);
    }

    private Result<String> setCategoryStatus(Long id, boolean active) {
        if (id == null) {
            return Result.error("分类 id 不能为空");
        }
        Integer categoryId = id.intValue();
        Category category = categoryMapper.selectById(categoryId);
        if (category == null || Boolean.TRUE.equals(category.getIsDeleted())) {
            log.error("查询数据库为空 setCategoryStatus -> id : {}", id);
            return Result.error("查询数据库为空 id : " + id);
        }
        Category update = new Category();
        update.setId(categoryId);
        update.setIsActive(active);
        update.setUpdatedAt(LocalDateTime.now());
        categoryMapper.updateById(update);
        // 清理历史分页缓存
        redissonUtil.deleteByPattern(RedissonKey.CATEGORY_PAGE_KEY + "*");
        return Result.success("设置成功");
    }

    /**
     * 删除（逻辑删除）
     */
    @Override
    public Result<String> deleteCategory(Integer id) {
        Category category = categoryMapper.selectById(id);
        if (category == null || Boolean.TRUE.equals(category.getIsDeleted())) {
            return Result.error("分类不存在");
        }
        Category update = new Category();
        update.setId(id);
        update.setIsDeleted(true);
        update.setUpdatedAt(LocalDateTime.now());
        categoryMapper.updateById(update);
        redissonUtil.deleteByPattern(RedissonKey.CATEGORY_PAGE_KEY + "*");
        return Result.success();
    }

    /**
     * 新增：level=1 无父分类；level=2 必须选同类型的 level=1 父分类；
     * 同一父分类 + 类型下名称不可重复。
     */
    @Override
    public Result<String> newCategory(NewCategoryDTO newCategoryDTO) {
        if (newCategoryDTO == null) {
            log.error("数据错误 --> newCategoryDTO == null");
            return Result.error("数据错误");
        }
        if (newCategoryDTO.getCategoryType() == null
                || newCategoryDTO.getName() == null
                || newCategoryDTO.getName().isBlank()
                || newCategoryDTO.getLevel() == null) {
            log.error("数据错误");
            return Result.error("数据错误");
        }
        if (newCategoryDTO.getLevel() != 1 && newCategoryDTO.getLevel() != 2) {
            return Result.error("分类层级只能是 1 或 2");
        }

        Integer parentId;
        if (newCategoryDTO.getLevel() == 1) {
            // 顶级分类强制无父分类
            parentId = 0;
        } else {
            parentId = newCategoryDTO.getParentId();
            if (parentId == null || parentId <= 0) {
                return Result.error("二级分类必须选择父分类");
            }
            Category parent = categoryMapper.selectById(parentId);
            if (parent == null || Boolean.TRUE.equals(parent.getIsDeleted())) {
                return Result.error("父分类不存在");
            }
            if (parent.getLevel() == null || parent.getLevel() != 1) {
                return Result.error("父分类必须是一级分类");
            }
            if (!Objects.equals(parent.getCategoryType(), newCategoryDTO.getCategoryType())) {
                return Result.error("父分类与分类类型不一致");
            }
        }

        String name = newCategoryDTO.getName().trim();
        QueryWrapper<Category> duplicateQuery = new QueryWrapper<>();
        duplicateQuery.eq("is_deleted", false)
                .eq("parent_id", parentId)
                .eq("category_type", newCategoryDTO.getCategoryType())
                .eq("name", name);
        if (categoryMapper.selectCount(duplicateQuery) > 0) {
            return Result.error("分类名称已存在");
        }

        Category category = Category.builder()
                .sortOrder(10)
                .isActive(false)
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .level(newCategoryDTO.getLevel())
                .name(name)
                .parentId(parentId)
                .categoryType(newCategoryDTO.getCategoryType())
                .path("/")
                .build();
        categoryMapper.insert(category);

        Category pathUpdate = new Category();
        pathUpdate.setId(category.getId());
        pathUpdate.setPath(category.getLevel() == 1
                ? "/" + category.getId()
                : "/" + category.getParentId() + "/" + category.getId());
        categoryMapper.updateById(pathUpdate);

        redissonUtil.deleteByPattern(RedissonKey.CATEGORY_PAGE_KEY + "*");
        return Result.success("新增成功");
    }
}
