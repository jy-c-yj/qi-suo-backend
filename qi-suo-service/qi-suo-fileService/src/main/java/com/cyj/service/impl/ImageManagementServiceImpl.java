package com.cyj.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.constant.RedissonKey;
import com.cyj.constants.RoleConstants;
import com.cyj.dto.CarouselImageDetailsDTO;
import com.cyj.dvo.CarouselImageDVO;
import com.cyj.dvo.HomeBackgroundImageDVO;
import com.cyj.entity.CarouselImage;
import com.cyj.entity.HomeBackgroundImage;
import com.cyj.feign.admin.AdminNameFeignClient;
import com.cyj.mapper.CarouselImageMapper;
import com.cyj.mapper.HomeBackgroundImageMapper;
import com.cyj.service.ImageManagementService;
import com.cyj.utils.MinIOUtils;
import com.cyj.utils.RedissonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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
    private CarouselImageMapper carouselImageMapper;

    @Autowired
    private HomeBackgroundImageMapper homeBackgroundImageMapper;

    @Autowired
    private AdminNameFeignClient adminNameFeignClient;

    @Autowired
    private RedissonUtil redissonUtil;

    /**
     * 取当前操作管理员名：管理员信息归管理服务所有，这里走服务间调用。
     * 注意 Feign 拦截器会把调用身份固定成 -1，所以管理员 id 必须作为参数显式传过去。
     */
    private Result<String> currentAdminName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Long)) {
            return Result.error("获取操作人失败");
        }
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        // 服务间调用（Feign 注入 system，principal 固定为 -1）：无需回查管理员表
        if (roles.contains(RoleConstants.SYSTEM)) {
            Result<String> systemResult = Result.success();
            systemResult.setData(RoleConstants.SYSTEM);
            return systemResult;
        }
        if (!roles.contains(RoleConstants.OrdinaryADMIN)
                && !roles.contains(RoleConstants.SuperADMIN)
                && !roles.contains(RoleConstants.SystemADMIN)) {
            return Result.error("获取操作人失败");
        }
        Result<String> adminNameResult = adminNameFeignClient.getAdminNameById((Long) authentication.getPrincipal());
        if (adminNameResult.getCode() == 0) {
            return Result.error("获取操作人失败");
        }
        return adminNameResult;
    }

    /**
     * 上传图片到对象存储并返回访问地址。
     * 原实现由管理服务经 FileFeign 调用文件服务的 POST /api/file/upload；
     * 图片管理并入文件服务后改为本地直调，逻辑与 UploadController#upload 保持一致
     * （失败时返回的 Result 与 Feign 降级回调 FileFeignBack 相同）。
     */
    private Result<String> uploadToMinio(MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("图片上传出错");
        }
        try {
            String originalFilename = file.getOriginalFilename();
            //获取文件类型
            String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1)
                    .toLowerCase();

            String fileName = UUID.randomUUID().toString() + "." + extension;

            switch (extension) {
                case "jpg":
                case "jpeg":
                    extension = "image/jpeg";
                    break;
                case "png":
                    extension = "image/png";
                    break;
                default:
                    break;
            }
            MinIOUtils.uploadFile("qisuo", file, fileName, extension);
            // URL 要放进 data：Result.success(String) 重载会把值塞进 msg，所以这里手工塞 data
            Result<String> result = Result.success();
            result.setData(MinIOUtils.getBasisUrl() + fileName);
            return result;
        } catch (Exception e) {
            return Result.error("文件上传失败");
        }
    }

    @Override
    public Result<List<CarouselImageDVO>> getCarouselImageList() {
        //直接查库，保证启用/禁用、删除后列表立即正确
        QueryWrapper<CarouselImage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", false);
        queryWrapper.orderByDesc("id");
        List<CarouselImage> carouselImageList = carouselImageMapper.selectList(queryWrapper);

        List<CarouselImageDVO> resultList = carouselImageList.stream()
                .map(image -> CarouselImageDVO.builder()
                        .id(image.getId())
                        .uid(image.getUid())
                        .url(image.getUrl())
                        .description(image.getDescription())
                        .isEnabled(image.getIsEnabled())
                        .createdTime(image.getCreatedTime())
                        .createdBy(image.getCreatedBy())
                        .modifiedTime(image.getModifiedTime())
                        .modifiedBy(image.getModifiedBy())
                        .build())
                .toList();

        //同步刷新缓存，保持原有缓存结构
        redissonUtil.delete(RedissonKey.IMAGE_CAROUSEL_KEY);
        resultList.forEach(dvo -> redissonUtil.addToList(RedissonKey.IMAGE_CAROUSEL_KEY, dvo));

        return Result.success(resultList);
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
            Result<String> result = uploadToMinio(file);
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
        //查找操作人（管理员信息由管理服务提供）
        Result<String> adminNameResult = currentAdminName();
        if(adminNameResult.getCode() == 0){
            return Result.error("获取操作人失败");
        }
        String adminName = adminNameResult.getData();

        carouselImage.setCreatedBy(adminName);
        carouselImage.setCreatedTime(LocalDateTime.now());
        carouselImage.setModifiedBy(adminName);
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

        //查找操作人（管理员信息由管理服务提供）
        Result<String> adminNameResult = currentAdminName();
        if(adminNameResult.getCode() == 0){
            return Result.error("获取操作人失败");
        }
        String adminName = adminNameResult.getData();

        //刷新缓存
        if(!carouselImageList.isEmpty() && !redissonUtil.getList(RedissonKey.IMAGE_CAROUSEL_KEY).isEmpty()){
            redissonUtil.delete(RedissonKey.IMAGE_CAROUSEL_KEY);
        }


        carouselImageList.forEach(image -> {
            if(image.getUid().equals(imageUid)){
                image.setIsEnabled(true);
                image.setModifiedBy(adminName);
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

        //查找操作人（管理员信息由管理服务提供）
        Result<String> adminNameResult = currentAdminName();
        if(adminNameResult.getCode() == 0){
            return Result.error("获取操作人失败");
        }
        String adminName = adminNameResult.getData();

        //刷新缓存
        if(!carouselImageList.isEmpty() && !redissonUtil.getList(RedissonKey.IMAGE_CAROUSEL_KEY).isEmpty()){
            redissonUtil.delete(RedissonKey.IMAGE_CAROUSEL_KEY);
        }

        carouselImageList.forEach(image -> {
            if(image.getUid().equals(imageUid)){
                image.setIsEnabled(false);
                image.setModifiedBy(adminName);
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

        //查找操作人（管理员信息由管理服务提供）
        Result<String> adminNameResult = currentAdminName();
        if(adminNameResult.getCode() == 0){
            return Result.error("获取操作人失败");
        }
        String adminName = adminNameResult.getData();

        //刷新缓存
        if(!carouselImageList.isEmpty() && !redissonUtil.getList(RedissonKey.IMAGE_CAROUSEL_KEY).isEmpty()){
            redissonUtil.delete(RedissonKey.IMAGE_CAROUSEL_KEY);
        }

        carouselImageList.forEach(image -> {
            if(image.getUid().equals(imageUid)){
                image.setIsDeleted(true);
                image.setModifiedBy(adminName);
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
        //查找操作人（管理员信息由管理服务提供）
        Result<String> adminNameResult = currentAdminName();
        if(adminNameResult.getCode() == 0){
            return Result.error("获取操作人失败");
        }
        carouselImage.setModifiedBy(adminNameResult.getData());
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
        //直接查库：原实现缓存未命中时返回空列表且 total 与分页不一致
        Page<HomeBackgroundImage> page = new Page<>(pageNum, pageSize);
        QueryWrapper<HomeBackgroundImage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted",false);
        queryWrapper.orderByDesc("id");
        Page<HomeBackgroundImage> homeBackgroundImagePage = homeBackgroundImageMapper.selectPage(page, queryWrapper);

        List<HomeBackgroundImageDVO> rows = homeBackgroundImagePage.getRecords().stream()
                .map(image -> HomeBackgroundImageDVO.builder()
                        .id(image.getId())
                        .url(image.getUrl())
                        .uid(image.getUid())
                        .isEnabled(image.getIsEnabled())
                        .isDeleted(image.getIsDeleted())
                        .createdTime(image.getCreatedTime())
                        .updatedTime(image.getUpdatedTime())
                        .description(image.getDescription())
                        .build())
                .toList();

        PageResult<HomeBackgroundImageDVO> pageResult = new PageResult<>();
        pageResult.setRows(rows);
        pageResult.setTotal(homeBackgroundImagePage.getTotal());

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

        //列表改为直接查库，这里只需清掉历史分页缓存
        redissonUtil.deleteByPattern(RedissonKey.IMAGE_HOMEBACKGROUND_KEY + "page:*");
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

        Result<String> result = uploadToMinio(file);
        if(result.getCode() == 0){
            return Result.error(result.getMsg());
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

        //列表改为直接查库，这里只需清掉历史分页缓存
        redissonUtil.deleteByPattern(RedissonKey.IMAGE_HOMEBACKGROUND_KEY + "page:*");

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
