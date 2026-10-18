package com.cyj.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.constant.RedissonKey;
import com.cyj.dto.*;
import com.cyj.dvo.BackgroundGetProductDVO;
import com.cyj.dvo.BackgroundGetProductDetailDVO;
import com.cyj.entity.Merchant;
import com.cyj.entity.Product;
import com.cyj.entity.ProductAttributeValue;
import com.cyj.entity.ProductCategory;
import com.cyj.exception.ProductStockIsNotException;
import com.cyj.mapper.*;
import com.cyj.service.MerchantInformationManagementService;
import com.cyj.service.MerchantProductManagementService;
import com.cyj.utils.RedissonUtil;
import com.cyj.utils.SkuUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MerchantProductManagementServiceImpl implements MerchantProductManagementService {

    @Autowired
    private RedissonUtil redissonUtil;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private MerchantMapper merchantMapper;


    @Autowired
    private ProductAttributeValueMapper productAttributeValueMapper;

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Override
    public Result<PageResult<BackgroundGetProductDVO>> getProduct(BackgroundGetProductByConditionDTO backgroundGetProductByConditionDTO) {
        if(backgroundGetProductByConditionDTO ==null){
            return Result.error("错误查询 null");
        }

        if(backgroundGetProductByConditionDTO.getPageNum() == null
                || backgroundGetProductByConditionDTO.getPageSize() == null){
            return Result.error("页码为空");
        }
        Long merchantId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Merchant merchant = merchantMapper.selectById(merchantId);
        backgroundGetProductByConditionDTO.setMerchantName(merchant.getMerchantName());
        //查询数据库
        Page<BackgroundGetProductDVO> page = new Page<>(backgroundGetProductByConditionDTO.getPageNum(), backgroundGetProductByConditionDTO.getPageSize());
        Page<BackgroundGetProductDVO> list = productMapper.getProductDVOList(page, backgroundGetProductByConditionDTO);

        PageResult<BackgroundGetProductDVO> pageResult = new PageResult<>();
        pageResult.setTotal(page.getTotal());
        pageResult.setRows(list.getRecords());

        return Result.success(pageResult);

    }


    /**
     * 查询商品详情
     * @param id
     * @return
     */
    @Override
    public Result<BackgroundGetProductDetailDVO> getProductDetail(Integer id) {
        if(id==null){
            return Result.error("查询错误，未知id : " + id);
        }
        String key = RedissonKey.PRODUCT_MANAGEMENT_DETAIL_KEY + id;

        //查询缓存
        BackgroundGetProductDetailDVO backgroundGetProductDetailDVO = redissonUtil.getValue(key);
        if(backgroundGetProductDetailDVO != null){
            return Result.success(backgroundGetProductDetailDVO);
        }

        BackgroundGetProductDetailDVO backgroundGetProductDetailDVO_ = null;

        //查询数据库
        try {
           backgroundGetProductDetailDVO_ = productMapper.selectProductDetailById(id);
        }catch (Exception e){
            log.error("数据库查询异常 getProductDetail : "+ e.getMessage());
            return Result.error("数据库查询异常 getProductDetail");
        }

        //更新缓存
        redissonUtil.setValue(key, backgroundGetProductDetailDVO_);

        return Result.success(backgroundGetProductDetailDVO_);
    }

    /**
     * 修改商品信息
     * @param merchantUpdateProductFormDTO
     * @return
     */
    @Override
    public Result<String> updateProduct(MerchantUpdateProductFormDTO merchantUpdateProductFormDTO) {
        if(merchantUpdateProductFormDTO==null){
            return Result.error("修改商品信息出错, 修改数据有误");
        }
        //获取数据
        Product product = productMapper.selectById(merchantUpdateProductFormDTO.getId());
        if(product==null){
            return Result.error("修改的商品不存在，productId -- > " + merchantUpdateProductFormDTO.getId());
        }
        if(merchantUpdateProductFormDTO.getProductName() != null && !merchantUpdateProductFormDTO.getProductName().isEmpty()){
            product.setProductName(merchantUpdateProductFormDTO.getProductName());
        }
        if(merchantUpdateProductFormDTO.getDescription() != null && !merchantUpdateProductFormDTO.getDescription().isEmpty()){
            product.setDescription(merchantUpdateProductFormDTO.getDescription());
        }
        if(merchantUpdateProductFormDTO.getProductName() != null && !merchantUpdateProductFormDTO.getProductName().isEmpty()){
            product.setProductName(merchantUpdateProductFormDTO.getProductName());
        }
        if(merchantUpdateProductFormDTO.getStock() != null ){
            product.setStock(merchantUpdateProductFormDTO.getStock());
        }
        if(merchantUpdateProductFormDTO.getPrice() != null ){
            product.setPrice(merchantUpdateProductFormDTO.getPrice());
        }
        if(merchantUpdateProductFormDTO.getOriginalPrice() != null ){
            product.setOriginalPrice(merchantUpdateProductFormDTO.getOriginalPrice());
        }
        if(merchantUpdateProductFormDTO.getIsNew() != null){
            product.setIsNew(merchantUpdateProductFormDTO.getIsNew());
        }
        if(merchantUpdateProductFormDTO.getIsHot() != null){
            product.setIsHot(merchantUpdateProductFormDTO.getIsHot());
        }
        // 图片只在明确传入非空值时覆盖，避免详情页其它字段保存时清空图片
        if(merchantUpdateProductFormDTO.getProductImageUrl() != null
                && !merchantUpdateProductFormDTO.getProductImageUrl().isBlank()){
            product.setProductImageUrl(merchantUpdateProductFormDTO.getProductImageUrl());
        }
        product.setUpdatedAt(LocalDateTime.now());
        try {
            productMapper.updateById(product);
        }catch (Exception e){
            log.error("修改出错，数据库修改出错");
            return Result.error("修改出错，数据库修改出错");
        }


        //删除缓存
        String key = RedissonKey.PRODUCT_MANAGEMENT_DETAIL_KEY + product.getId();
        redissonUtil.delete(key);

        return Result.success();
    }

    /**
     * 新增商品
     * @param newProductDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> newProduct(NewProductDTO newProductDTO) {
        if(newProductDTO==null){
            return Result.error("新增失败，数据为空");
        }
        //新增数据库
        Product product = new Product();
        product.setProductName(newProductDTO.getProductName());
        product.setDescription(newProductDTO.getDescription());
        product.setStock(newProductDTO.getStock());
        product.setOriginalPrice(newProductDTO.getOriginalPrice());
        product.setPrice(newProductDTO.getPrice());
        product.setProductImageUrl(newProductDTO.getProductImageUrl());
        product.setIsNew(newProductDTO.getIsNew() != null && newProductDTO.getIsNew());
        product.setIsHot(newProductDTO.getIsHot() != null && newProductDTO.getIsHot());
        // 新商品默认上架
        product.setStatus((byte) 1);
        product.setIsDeleted(false);

        String SKU = SkuUtil.generateBySequence();
        product.setSku(SKU);

        Long merchantID = (Long)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        product.setMerchantId(merchantID);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        //插入基本信息
        productMapper.insert(product);

        //设置分类
        List<ProductCategory> list1 = new ArrayList<>();
        for(Integer id: newProductDTO.getCategoryIds() ){
            ProductCategory productCategory = new ProductCategory();
            productCategory.setCategoryId(id);
            productCategory.setProductId(product.getId());
            productCategory.setCreatedAt(LocalDateTime.now());
            list1.add(productCategory);
        }
        productCategoryMapper.insert(list1);

        //设置属性
        List<ProductAttributeValue> list2 = new ArrayList<>();
        for(Integer id: newProductDTO.getAttributeAndValues() ){
            ProductAttributeValue productAttributeValue = new ProductAttributeValue();
            productAttributeValue.setProductId(product.getId());
            productAttributeValue.setAttribute_valueId(id);
            productAttributeValue.setCreatedAt(LocalDateTime.now());
            list2.add(productAttributeValue);
        }
        productAttributeValueMapper.insert(list2);


        return Result.success();
    }

    /**
     * 新增商品分类
     * @param newProductCategoryDTO
     * @return
     */
    @Override
    public Result<String> newProductCategory(NewProductCategoryDTO newProductCategoryDTO) {
        if(newProductCategoryDTO==null){
            return Result.error("新增分类失败");
        }
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryId(newProductCategoryDTO.getCategoryId());
        productCategory.setProductId(newProductCategoryDTO.getProductId());
        productCategory.setCreatedAt(LocalDateTime.now());
        try {
            productCategoryMapper.insert(productCategory);
            //删除缓存
            String key = RedissonKey.PRODUCT_MANAGEMENT_DETAIL_KEY + productCategory.getProductId();
            redissonUtil.delete(key);
            return Result.success("新增成功");
        } catch (DuplicateKeyException e) {
            // 处理主键冲突异常
            return Result.error("该商品已属于此分类");
        } catch (Exception e) {
            // 处理其他异常
            log.error("新增商品分类失败", e);
            return Result.error("新增商品分类失败，请稍后重试");
        }

    }

    /**
     * 删除商品分类
     * @param deleteProductCategoryDTO
     * @return
     */
    @Override
    public Result<String> deleteProductCategory(DeleteProductCategoryDTO deleteProductCategoryDTO) {
        if(deleteProductCategoryDTO == null){
            return Result.error("删除商品分类失败");
        }
        QueryWrapper<ProductCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", deleteProductCategoryDTO.getProductId()).eq("category_id", deleteProductCategoryDTO.getCategoryId());
        productCategoryMapper.delete(queryWrapper);
        String key = RedissonKey.PRODUCT_MANAGEMENT_DETAIL_KEY + deleteProductCategoryDTO.getProductId();
        redissonUtil.delete(key);
        return Result.success("删除成功");
    }

    /**
     * 新增属性
     * @param newProductAttributeValueDTO
     * @return
     */
    @Override
    public Result<String> newProductAttributeValue(NewProductAttributeValueDTO newProductAttributeValueDTO) {
        if(newProductAttributeValueDTO == null){
            return Result.error("新增属性失败");
        }
        if(newProductAttributeValueDTO.getProductId() == null){
            return Result.error("商品ID不能为空");
        }
        if(newProductAttributeValueDTO.getAttributeValueId() == null){
            return Result.error("请选择要添加的属性值");
        }
        ProductAttributeValue productAttributeValue = new ProductAttributeValue();
        productAttributeValue.setProductId(newProductAttributeValueDTO.getProductId());
        productAttributeValue.setAttribute_valueId(newProductAttributeValueDTO.getAttributeValueId());
        productAttributeValue.setCreatedAt(LocalDateTime.now());
        try {
            productAttributeValueMapper.insert(productAttributeValue);
            String key = RedissonKey.PRODUCT_MANAGEMENT_DETAIL_KEY + newProductAttributeValueDTO.getProductId();
            redissonUtil.delete(key);
            return Result.success("新增成功");
        } catch (DuplicateKeyException e) {
            // 处理主键冲突异常
            log.warn("重复添加商品属性值：productId={}, attributeValueId={}",
                    newProductAttributeValueDTO.getProductId(),
                    newProductAttributeValueDTO.getAttributeValueId());
            return Result.error("该商品已存在此属性值");
        } catch (Exception e) {
            // 处理其他异常
            log.error("新增商品属性值失败：productId={}, attributeValueId={}",
                    newProductAttributeValueDTO.getProductId(),
                    newProductAttributeValueDTO.getAttributeValueId(), e);
            return Result.error("新增属性值失败，请稍后重试");
        }

    }

    /**
     * 删除商品属性
     * @param deleteProductAttributeValueDTO
     * @return
     */
    @Override
    public Result<String> deleteProductAttributeValue(DeleteProductAttributeValueDTO deleteProductAttributeValueDTO) {
        if(deleteProductAttributeValueDTO == null){
            return Result.error("删除商品属性失败");
        }
        QueryWrapper<ProductAttributeValue> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", deleteProductAttributeValueDTO.getProductId()).eq("attribute_value_id", deleteProductAttributeValueDTO.getAttributeValueId());
        productAttributeValueMapper.delete(queryWrapper);
        String key = RedissonKey.PRODUCT_MANAGEMENT_DETAIL_KEY + deleteProductAttributeValueDTO.getProductId();
        redissonUtil.delete(key);
        return Result.success("删除商品属性成功");
    }

    /**
     * 设置上、下架状态
     * @return
     */
    @Override
    public Result<String> setStatus(Integer productId, Integer status) {
        if(productId==null){
            return Result.error("设置失败状态异常");
        }
        //查询数据库
        Product product = productMapper.selectById(productId);
        if(product==null){
            return Result.error("商品不存在");
        }
        if (status != null) {
            // 显式方向：1=上架，0=下架
            if (status != 0 && status != 1) {
                return Result.error("状态值不合法");
            }
            product.setStatus(status.byteValue());
        } else if(product.getStatus() != null && product.getStatus() == 1){
            product.setStatus((byte) 0);
        }else {
            product.setStatus((byte) 1);
        }
        product.setUpdatedAt(LocalDateTime.now());
        //更新数据
        productMapper.updateById(product);
        //删除缓存
        String key = RedissonKey.PRODUCT_MANAGEMENT_DETAIL_KEY + product.getId();
        redissonUtil.delete(key);
        return Result.success("设置成功");
    }

    /**
     * 删除商品
     * @param productId
     * @return
     */
    @Override
    public Result<String> deleteProduct(Integer productId) {
        if(productId==null){
            return Result.error("删除商品异常");
        }
        Product product = productMapper.selectById(productId);
        if(product==null){
            return Result.error("商品不存在");
        }
        product.setIsDeleted(true);
        productMapper.updateById(product);
        //删除缓存
        String key = RedissonKey.PRODUCT_MANAGEMENT_DETAIL_KEY + product.getId();
        redissonUtil.delete(key);

        return Result.success("删除成功");
    }

    /**
     * 整单扣减库存。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> deductStock(ProductStockDTO productStockDTO) {
        Result<String> invalid = validateStockItems(productStockDTO);
        if (invalid != null) {
            return invalid;
        }
        //校验所有商品的可用库存。
        for (ProductStockItemDTO item : productStockDTO.getItems()) {
            Product product = productMapper.selectById(item.getProductId());
            if (product == null) {
                return Result.error("商品不存在：" + item.getProductId());
            }
            if (product.getStock() == null || product.getStock() < item.getQuantity()) {
                return Result.error("商品库存不足：" + item.getProductId());
            }
        }
        for (ProductStockItemDTO item : productStockDTO.getItems()) {
            if (productMapper.deductStock(item.getProductId(), item.getQuantity()) != 1) {
                //并发下被别人扣走了，同样整批回滚
                throw new ProductStockIsNotException("商品库存不足：" + item.getProductId());
            }
            redissonUtil.delete(RedissonKey.PRODUCT_MANAGEMENT_DETAIL_KEY + item.getProductId());
        }
        log.info("订单 {} 扣减库存成功", productStockDTO.getOrderId());
        return Result.success("扣减库存成功");
    }

    /**
     * 整单归还库存（订单超时取消）。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> restoreStock(ProductStockDTO productStockDTO) {
        Result<String> invalid = validateStockItems(productStockDTO);
        if (invalid != null) {
            return invalid;
        }
        for (ProductStockItemDTO item : productStockDTO.getItems()) {
            productMapper.restoreStock(item.getProductId(), item.getQuantity());
            redissonUtil.delete(RedissonKey.PRODUCT_MANAGEMENT_DETAIL_KEY + item.getProductId());
        }
        log.info("订单 {} 归还库存成功", productStockDTO.getOrderId());
        return Result.success("归还库存成功");
    }

    private Result<String> validateStockItems(ProductStockDTO productStockDTO) {
        if (productStockDTO == null || productStockDTO.getItems() == null || productStockDTO.getItems().isEmpty()) {
            return Result.error("库存变更参数为空");
        }
        for (ProductStockItemDTO item : productStockDTO.getItems()) {
            if (item == null || item.getProductId() == null
                    || item.getQuantity() == null || item.getQuantity() <= 0) {
                return Result.error("库存变更参数非法");
            }
        }
        return null;
    }
}
