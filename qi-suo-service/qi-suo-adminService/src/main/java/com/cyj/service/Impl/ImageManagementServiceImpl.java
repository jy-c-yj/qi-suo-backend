package com.cyj.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.constant.RedissonKey;
import com.cyj.dto.CarouselImageDetailsDTO;
import com.cyj.dvo.CarouselImageDVO;
import com.cyj.dvo.HomeBackgroundImageDVO;
import com.cyj.entity.Admin;
import com.cyj.entity.CarouselImage;
import com.cyj.entity.HomeBackgroundImage;
import com.cyj.feign.file.FileFeign;
import com.cyj.mapper.AdminMapper;
import com.cyj.mapper.CarouselImageMapper;
import com.cyj.mapper.HomeBackgroundImageMapper;
import com.cyj.service.ImageManagementService;
import com.cyj.utils.RedissonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


/**
 * 图像管理服务类实现
 */
@Service
public class ImageManagementServiceImpl implements ImageManagementService {
    @Autowired
    private FileFeign fileFeign;

    @Autowired
    private CarouselImageMapper carouselImageMapper;

    @Autowired
    private HomeBackgroundImageMapper homeBackgroundImageMapper;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private RedissonUtil redissonUtil;
    @Override
    public Result<List<CarouselImageDVO>> getCarouselImageList() {
        //查询redis
        String key = RedissonKey.IMAGE_CAROUSEL_KEY;
        List<CarouselImageDVO> carouselImageDVOList = redissonUtil.getList(key);

        //存在缓存直接返回

        if(!carouselImageDVOList.isEmpty()){
            return Result.success(carouselImageDVOList);
        }

        //不存在查询数据库
        QueryWrapper<CarouselImage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted",false);
        List<CarouselImage> liscarouselImagList = carouselImageMapper.selectList(queryWrapper);



        //查询成功存入缓存
        if (!liscarouselImagList.isEmpty()) {
            liscarouselImagList.forEach(image -> {
                CarouselImageDVO carouselImageDVO = CarouselImageDVO.builder()
                        .id(image.getId())
                        .uid(image.getUid())
                        .url(image.getUrl())
                        .description(image.getDescription())
                        .isEnabled(image.getIsEnabled())
                        .createdTime(image.getCreatedTime())
                        .createdBy(image.getCreatedBy())
                        .modifiedTime(image.getModifiedTime())
                        .modifiedBy(image.getModifiedBy())
                        .build();
                redissonUtil.addToList(RedissonKey.IMAGE_CAROUSEL_KEY, carouselImageDVO);
            });
        }

        return Result.success(carouselImageDVOList);
    }

    /**
     * 新增轮播图片
     * @param file
     * @param description
     * @return
     */
    @Override
    public Result<String> newCarouselImage(MultipartFile file, String description) {
        String url = null;
        try {
            Result<String> result = fileFeign.upload(file);
            if(result.getCode() == 0){
                return Result.error(result.getMsg());
            }
            url = result.getData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //存入数据库
        CarouselImage carouselImage = new CarouselImage();
        carouselImage.setDescription(description);
        carouselImage.setUrl(url);
        carouselImage.setUid(file.getOriginalFilename() + UUID.randomUUID());
        carouselImage.setIsEnabled(true);
        //查找操作人
        Long id = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Admin admin = adminMapper.selectById(id);

        carouselImage.setCreatedBy(admin.getUsername());
        carouselImage.setCreatedTime(LocalDateTime.now());
        carouselImage.setModifiedBy(admin.getUsername());
        carouselImage.setModifiedTime(LocalDateTime.now());

        carouselImageMapper.insert(carouselImage);

        //存入缓存
        CarouselImageDVO carouselImageDVO = CarouselImageDVO.builder()
                .id(carouselImage.getId())
                .uid(carouselImage.getUid())
                .url(carouselImage.getUrl())
                .description(carouselImage.getDescription())
                .isEnabled(carouselImage.getIsEnabled())
                .createdTime(carouselImage.getCreatedTime())
                .createdBy(carouselImage.getCreatedBy())
                .modifiedTime(carouselImage.getModifiedTime())
                .modifiedBy(carouselImage.getModifiedBy())
                .build();

        redissonUtil.addToList(RedissonKey.IMAGE_CAROUSEL_KEY, carouselImageDVO);
        return Result.success("新增成功");
    }

    /**
     * 轮播图片设为启用
     * @param imageUid
     * @return
     */
    @Override
    public Result<String> setCarouselImageEnabled(String imageUid) {
        QueryWrapper<CarouselImage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted",false);
        List<CarouselImage> carouselImageList = carouselImageMapper.selectList(queryWrapper);

        //查找操作人
        Long id = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Admin admin = adminMapper.selectById(id);

        //刷新缓存
        if(!carouselImageList.isEmpty() && !redissonUtil.getList(RedissonKey.IMAGE_CAROUSEL_KEY).isEmpty()){
            redissonUtil.delete(RedissonKey.IMAGE_CAROUSEL_KEY);
        }


        carouselImageList.forEach(image -> {
            if(image.getUid().equals(imageUid)){
                image.setIsEnabled(true);
                image.setModifiedBy(admin.getUsername());
                //更新数据库
                QueryWrapper<CarouselImage> queryWrapper_ = new QueryWrapper<>();
                queryWrapper_.eq("uid",imageUid);
                carouselImageMapper.update(image,queryWrapper_);
            }
            CarouselImageDVO carouselImageDVO = CarouselImageDVO.builder()
                    .id(image.getId())
                    .uid(image.getUid())
                    .url(image.getUrl())
                    .description(image.getDescription())
                    .isEnabled(image.getIsEnabled())
                    .createdTime(image.getCreatedTime())
                    .createdBy(image.getCreatedBy())
                    .modifiedTime(image.getModifiedTime())
                    .modifiedBy(image.getModifiedBy())
                    .build();
            redissonUtil.addToList(RedissonKey.IMAGE_CAROUSEL_KEY, carouselImageDVO);

        });


        return Result.success("设置成功");
    }
    /**
     * 轮播图片设为禁用
     * @param imageUid
     * @return
     */
    @Override
    public Result<String> setCarouselImageNotEnabled(String imageUid) {
        QueryWrapper<CarouselImage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted",false);
        List<CarouselImage> carouselImageList = carouselImageMapper.selectList(queryWrapper);

        //查找操作人
        Long id = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Admin admin = adminMapper.selectById(id);

        //刷新缓存
        if(!carouselImageList.isEmpty() && !redissonUtil.getList(RedissonKey.IMAGE_CAROUSEL_KEY).isEmpty()){
            redissonUtil.delete(RedissonKey.IMAGE_CAROUSEL_KEY);
        }

        carouselImageList.forEach(image -> {
            if(image.getUid().equals(imageUid)){
                image.setIsEnabled(false);
                image.setModifiedBy(admin.getUsername());
                //更新数据库
                QueryWrapper<CarouselImage> queryWrapper_ = new QueryWrapper<>();
                queryWrapper_.eq("uid",imageUid);
                carouselImageMapper.update(image,queryWrapper_);
            }
            CarouselImageDVO carouselImageDVO = CarouselImageDVO.builder()
                    .id(image.getId())
                    .uid(image.getUid())
                    .url(image.getUrl())
                    .description(image.getDescription())
                    .isEnabled(image.getIsEnabled())
                    .createdTime(image.getCreatedTime())
                    .createdBy(image.getCreatedBy())
                    .modifiedTime(image.getModifiedTime())
                    .modifiedBy(image.getModifiedBy())
                    .build();
            redissonUtil.addToList(RedissonKey.IMAGE_CAROUSEL_KEY, carouselImageDVO);

        });

        return Result.success("设置成功");
    }

    /**
     * 删除轮播图片
     * @param imageUid
     * @return
     */
    @Override
    public Result<String> deleteCarouselImage(String imageUid) {
        QueryWrapper<CarouselImage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted",false);
        List<CarouselImage> carouselImageList = carouselImageMapper.selectList(queryWrapper);

        //查找操作人
        Long id = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Admin admin = adminMapper.selectById(id);

        //刷新缓存
        if(!carouselImageList.isEmpty() && !redissonUtil.getList(RedissonKey.IMAGE_CAROUSEL_KEY).isEmpty()){
            redissonUtil.delete(RedissonKey.IMAGE_CAROUSEL_KEY);
        }

        carouselImageList.forEach(image -> {
            if(image.getUid().equals(imageUid)){
                image.setIsDeleted(true);
                image.setModifiedBy(admin.getUsername());
                //更新数据库
                QueryWrapper<CarouselImage> queryWrapper_ = new QueryWrapper<>();
                queryWrapper_.eq("uid",imageUid);
                carouselImageMapper.update(image,queryWrapper_);
            }
            CarouselImageDVO carouselImageDVO = CarouselImageDVO.builder()
                    .id(image.getId())
                    .uid(image.getUid())
                    .url(image.getUrl())
                    .description(image.getDescription())
                    .isEnabled(image.getIsEnabled())
                    .createdTime(image.getCreatedTime())
                    .createdBy(image.getCreatedBy())
                    .modifiedTime(image.getModifiedTime())
                    .modifiedBy(image.getModifiedBy())
                    .build();
            redissonUtil.addToList(RedissonKey.IMAGE_CAROUSEL_KEY, carouselImageDVO);

        });

        return Result.success("删除成功");
    }

    /**
     * 修改图片信息
     * @param carouselImageDetailsDTO
     * @return
     */
    @Override
    public Result<String> updateCarouselImage(CarouselImageDetailsDTO carouselImageDetailsDTO) {
        QueryWrapper<CarouselImage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid",carouselImageDetailsDTO.getUid());

        CarouselImage carouselImage = new CarouselImage();
        carouselImage.setUid(carouselImageDetailsDTO.getUid());
        carouselImage.setUrl(carouselImageDetailsDTO.getUrl());
        carouselImage.setDescription(carouselImageDetailsDTO.getDescription());
        carouselImage.setIsEnabled(carouselImageDetailsDTO.getIsEnabled());
        carouselImage.setCreatedTime(carouselImageDetailsDTO.getCreatedTime());
        carouselImage.setModifiedTime(LocalDateTime.now());
        carouselImage.setCreatedBy(carouselImageDetailsDTO.getCreatedBy());
        //查找操作人
        Long id = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Admin admin = adminMapper.selectById(id);
        carouselImage.setModifiedBy(admin.getUsername());
        carouselImageMapper.update(carouselImage,queryWrapper);

        //更新缓存
        QueryWrapper<CarouselImage> queryWrapper_ = new QueryWrapper<>();
        queryWrapper.eq("is_deleted",false);
        List<CarouselImage> carouselImageList = carouselImageMapper.selectList(queryWrapper_);

        if(!carouselImageList.isEmpty() && !redissonUtil.getList(RedissonKey.IMAGE_CAROUSEL_KEY).isEmpty()){
            redissonUtil.delete(RedissonKey.IMAGE_CAROUSEL_KEY);
        }


        carouselImageList.forEach(image -> {
            CarouselImageDVO carouselImageDVO = CarouselImageDVO.builder()
                    .id(image.getId())
                    .uid(image.getUid())
                    .url(image.getUrl())
                    .description(image.getDescription())
                    .isEnabled(image.getIsEnabled())
                    .createdTime(image.getCreatedTime())
                    .createdBy(image.getCreatedBy())
                    .modifiedTime(image.getModifiedTime())
                    .modifiedBy(image.getModifiedBy())
                    .build();
            redissonUtil.addToList(RedissonKey.IMAGE_CAROUSEL_KEY, carouselImageDVO);

        });

        return Result.success("编辑成功");
    }

    /**
     * 主页背景图像管理
     */
    @Override
    public Result<PageResult<HomeBackgroundImageDVO>> getHomeBackGroundImageList(int pageNum,Long pageSize) {
        //查询缓存
        String key = RedissonKey.IMAGE_HOMEBACKGROUND_KEY+"page:"+pageNum;
        List<HomeBackgroundImageDVO> homeBackGroundImageDVOList = redissonUtil.getList(key);

        //存在直接返回
        if(!homeBackGroundImageDVOList.isEmpty()){
            PageResult<HomeBackgroundImageDVO> pageResult = new PageResult<>();
            pageResult.setRows(homeBackGroundImageDVOList);
            pageResult.setTotal((long) homeBackGroundImageDVOList.size());
            return Result.success(pageResult);
        }

        //不存在查询数据库
        Page<HomeBackgroundImage> page = new Page<>(pageNum, pageSize);
        QueryWrapper<HomeBackgroundImage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted",false);
        queryWrapper.orderByDesc("id");
     //   List<HomeBackgroundImage> homeBackgroundImageList = homeBackgroundImageMapper.selectList(queryWrapper);
        Page<HomeBackgroundImage> homeBackgroundImagePage = homeBackgroundImageMapper.selectPage(page, queryWrapper);
        List<HomeBackgroundImage> homeBackgroundImageList = homeBackgroundImagePage.getRecords();
        Long total = homeBackgroundImagePage.getTotal();

        //查询成功存入缓存
        homeBackgroundImageList.forEach(image -> {
            redissonUtil.addToList(key, HomeBackgroundImageDVO.builder()
                    .id(image.getId())
                    .url(image.getUrl())
                    .uid(image.getUid())
                    .isEnabled(image.getIsEnabled())
                    .isDeleted(image.getIsDeleted())
                    .createdTime(image.getCreatedTime())
                    .updatedTime(image.getUpdatedTime())
                    .description(image.getDescription())
                    .build());
        });

        //构建返回值
        PageResult<HomeBackgroundImageDVO> pageResult = new PageResult<>();
        pageResult.setRows(homeBackGroundImageDVOList);
        pageResult.setTotal(total);

        return Result.success(pageResult);
    }

    /**
     * 设置启用
     * @param imageUid
     * @return
     */
    @Override
    public Result<String> setHomeBackGroundImageEnabled(String imageUid,int pageNum) {
        return HomeBackGroundImageIsOrNotEnabled(imageUid, true,pageNum);
    }

    /**
     * 设置禁用
     * @param imageUid
     * @return
     */
    @Override
    public Result<String> setHomeBackGroundImageNotEnabled(String imageUid,int pageNum) {
        return HomeBackGroundImageIsOrNotEnabled(imageUid, false,pageNum);
    }

    /**
     * 设置背景图片禁用or启用
     * @param imageUid
     * @return
     */
    private Result<String> HomeBackGroundImageIsOrNotEnabled(String imageUid,boolean isEnabled,int pageNum) {
        QueryWrapper<HomeBackgroundImage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", imageUid);
        HomeBackgroundImage homeBackgroundImage = new HomeBackgroundImage();
        homeBackgroundImage.setIsEnabled(isEnabled);
        homeBackgroundImage.setUpdatedTime(LocalDateTime.now());
        homeBackgroundImageMapper.update(homeBackgroundImage, queryWrapper);

        String key = RedissonKey.IMAGE_HOMEBACKGROUND_KEY+"page:"+pageNum;
        //获取缓存
        List<HomeBackgroundImageDVO> homeBackgroundImageDVOList = redissonUtil.getList(key);

        for (int i = 0; i < homeBackgroundImageDVOList.size(); i++) {
                HomeBackgroundImageDVO homeBackgroundImageDVO = homeBackgroundImageDVOList.get(i);
                if(homeBackgroundImageDVO.getUid().equals(imageUid)){
                    homeBackgroundImageDVO.setIsEnabled(isEnabled);
                    homeBackgroundImageDVO.setUpdatedTime(homeBackgroundImage.getUpdatedTime());
                    homeBackgroundImageDVOList.set(i, homeBackgroundImageDVO);
                }
        }
        return Result.success("设置成功");
    }

    /**
     * 新增
     * @param file
     * @param description
     * @return
     */
    @Override
    public Result<String> newHomeBackGroundImage(MultipartFile file, String description) {

        Result<String> result = fileFeign.upload(file);
        if(result.getCode() == 0){
            return Result.error(description);
        }
        String url = result.getData();

        //插入数据库
        HomeBackgroundImage homeBackgroundImage = new HomeBackgroundImage();
        homeBackgroundImage.setDescription(description);
        homeBackgroundImage.setIsEnabled(false);
        homeBackgroundImage.setIsDeleted(false);
        homeBackgroundImage.setUid(UUID.randomUUID().toString());
        homeBackgroundImage.setUrl(url);
        homeBackgroundImage.setCreatedTime(LocalDateTime.now());
        homeBackgroundImage.setUpdatedTime(LocalDateTime.now());
        homeBackgroundImageMapper.insert(homeBackgroundImage);

        //获取最新背景图列表
        QueryWrapper<HomeBackgroundImage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted",false);
        List<HomeBackgroundImage> homeBackgroundImageList = homeBackgroundImageMapper.selectList(queryWrapper);

        //删除缓存
        redissonUtil.deleteByPattern(RedissonKey.IMAGE_HOMEBACKGROUND_KEY+"page:*");

        return Result.success("新增成功");
    }

    /**
     * 删除
     * @param imageUid
     * @return
     */
    @Override
    public Result<String> deleteHomeBackGroundImage(String imageUid) {
        QueryWrapper<HomeBackgroundImage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid",imageUid);
        HomeBackgroundImage homeBackgroundImage = new HomeBackgroundImage();
        homeBackgroundImage.setIsDeleted(true);
        homeBackgroundImage.setUid(imageUid);
        homeBackgroundImageMapper.update(homeBackgroundImage, queryWrapper);

        //删除缓存
        redissonUtil.deleteByPattern(RedissonKey.IMAGE_HOMEBACKGROUND_KEY+"page:*");

        return Result.success("删除成功");
    }

    /**
     * 编辑修改
     * @param homeBackgroundImageDVO
     * @return
     */
    @Override
    public Result<String> updateHomeBackGroundImage(HomeBackgroundImageDVO homeBackgroundImageDVO,int pageNum) {
        HomeBackgroundImage homeBackgroundImage = new HomeBackgroundImage();
        homeBackgroundImage.setId(homeBackgroundImageDVO.getId());
        homeBackgroundImage.setDescription(homeBackgroundImageDVO.getDescription());
        homeBackgroundImage.setIsDeleted(homeBackgroundImageDVO.getIsDeleted());
        homeBackgroundImage.setUid(homeBackgroundImageDVO.getUid());
        homeBackgroundImage.setUpdatedTime(LocalDateTime.now());
        homeBackgroundImage.setCreatedTime(LocalDateTime.now());
        homeBackgroundImageMapper.updateById(homeBackgroundImage);


        //更新缓存
        List<HomeBackgroundImageDVO> list = redissonUtil.getList(RedissonKey.IMAGE_HOMEBACKGROUND_KEY+"page:"+pageNum);

        for (int i = 0; i < list.size(); i++) {
            HomeBackgroundImageDVO homeBackgroundImageDVO1 = list.get(i);
            if(homeBackgroundImageDVO1.getUid().equals(homeBackgroundImageDVO.getUid())){
                list.set(i, homeBackgroundImageDVO);
            }
        }

        return Result.success("编辑成功");
    }

    /**
     * 获取当前背景图片
     * @return
     */
    @Override
    public Result<HomeBackgroundImageDVO> getNowHomeBackGroundImage() {
        //从缓存获取数据
        HomeBackgroundImageDVO homeBackgroundImageDVO = redissonUtil.getValue(RedissonKey.IMAGE_HOMEBACKGROUND_NOW_KEY);
        if(homeBackgroundImageDVO == null){
            Result<HomeBackgroundImageDVO> result = Result.success(null);
            result.setMsg("尚未找到被设置的图片");
            return result;
        }
        return Result.success(homeBackgroundImageDVO);
    }


    /**
     * 取消当前背景图片
     */
    @Override
    public Result<String> deleteNowHomeBackGroundImage(String uid) {

        HomeBackgroundImageDVO homeBackgroundImageDVO = redissonUtil.getValue(RedissonKey.IMAGE_HOMEBACKGROUND_NOW_KEY);
        if(homeBackgroundImageDVO == null){
            return Result.error("错误操作,未设置图片");
        }
        if(!homeBackgroundImageDVO.getUid().equals(uid)){
            return Result.error("错误操作,未找到对应图片");
        }
        redissonUtil.delete(RedissonKey.IMAGE_HOMEBACKGROUND_NOW_KEY);

        return Result.success("已经成功取消");
    }

    /**
     *设置已启用图片为当前背景图片
     */
    @Override
    public Result<String> setNowHomeBackGroundImage(String uid) {
        //判断是否已经存在当前背景图片
        HomeBackgroundImageDVO homeBackgroundImageDVO = redissonUtil.getValue(RedissonKey.IMAGE_HOMEBACKGROUND_NOW_KEY);
        if(homeBackgroundImageDVO != null){
            return Result.success("当前已存在正在使用的背景图片");
        }
        //查询背景图片
        QueryWrapper<HomeBackgroundImage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted",false);
        queryWrapper.eq("uid",uid);

        HomeBackgroundImage homeBackgroundImage = homeBackgroundImageMapper.selectOne(queryWrapper);


        //判断是否存在
        if(homeBackgroundImage == null){
            return Result.success("该图片不存在");
        }
        //判断是否启用
        if(!homeBackgroundImage.getIsEnabled()){
            return Result.success("该图片尚未启用");
        }

        //设置背景图片
        redissonUtil.setValue(RedissonKey.IMAGE_HOMEBACKGROUND_NOW_KEY, HomeBackgroundImageDVO.builder()
                        .description(homeBackgroundImage.getDescription())
                        .isDeleted(homeBackgroundImage.getIsDeleted())
                        .isEnabled(homeBackgroundImage.getIsEnabled())
                        .uid(homeBackgroundImage.getUid())
                        .id(homeBackgroundImage.getId())
                        .url(homeBackgroundImage.getUrl())
                        .createdTime(homeBackgroundImage.getCreatedTime())
                        .updatedTime(homeBackgroundImage.getUpdatedTime())
                .build()
        );
        return Result.success("设置成功");
    }
}
