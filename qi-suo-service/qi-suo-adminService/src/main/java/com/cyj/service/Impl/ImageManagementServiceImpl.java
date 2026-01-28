package com.cyj.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cyj.common.Result;
import com.cyj.constant.RedissonKey;
import com.cyj.dto.CarouselImageDetailsDTO;
import com.cyj.dvo.CarouselImageDVO;
import com.cyj.entity.Admin;
import com.cyj.entity.CarouselImage;
import com.cyj.feign.file.FileFeign;
import com.cyj.mapper.AdminMapper;
import com.cyj.mapper.CarouselImageMapper;
import com.cyj.service.ImageManagementService;
import com.cyj.utils.RedissonUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Date;
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
}
