package com.cyj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.constant.RedissonKey;
import com.cyj.dto.QueryProductByConditionDTO;
import com.cyj.dvo.BackgroundGetProductDetailDVO;
import com.cyj.dvo.HotProductDVO;
import com.cyj.dvo.QueryProductDVO;
import com.cyj.entity.Product;
import com.cyj.exception.UserProductConditionQueryIsEmptyException;
import com.cyj.mapper.ProductMapper;
import com.cyj.service.UserProductService;
import com.cyj.utils.RedissonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class UserProductServiceImpl implements UserProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private RedissonUtil redissonUtil;
    /**
     * 查询热门商品方法
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 返回包含热门商品分页结果的Result对象
     */
    @Override
    public Result<PageResult<HotProductDVO>> queryHotProduct(Integer pageNum, Integer pageSize){
        log.info("查询热门商品");
    // 构建Redis缓存键，包含页码信息
        String key = RedissonKey.HOT_PRODUCT_KEY+pageNum;
        //查询缓存
        List<HotProductDVO> hotProductDVOList = redissonUtil.getList(key);
    // 检查缓存是否存在数据
        if (hotProductDVOList != null && !hotProductDVOList.isEmpty()) {
            log.info("查询缓存成功");
            //存在返回
            PageResult<HotProductDVO> pageResult = new PageResult<>();
            pageResult.setRows(hotProductDVOList);
            pageResult.setTotal(Integer.toUnsignedLong(hotProductDVOList.size()) );
            return Result.success(pageResult);
        }
        //不存在查询数据库
        Page<HotProductDVO> page = new Page<>(pageNum, pageSize);
        try {
            List<HotProductDVO> hotProductDVOList1 = productMapper.queryHotProduct(page);
            //设置缓存
            redissonUtil.addAllToList(key, hotProductDVOList1);
            //返回
            PageResult<HotProductDVO> pageResult = new PageResult<>();
            pageResult.setRows(hotProductDVOList1);
            pageResult.setTotal(Integer.toUnsignedLong(hotProductDVOList1.size()));
            return Result.success(pageResult);
        } catch (Exception e) {
            log.error("查询热门商品失败", e);
            return Result.error("查询热门商品失败");
        }
    }

    @Override
    /**
     * 查询商品详情信息
     * @param id 商品ID
     * @return 返回商品详情信息的结果对象
     */
    public Result<BackgroundGetProductDetailDVO> queryProductDetail(Integer id) {
        // 构建Redis缓存的键，使用商品ID作为唯一标识
        String key = RedissonKey.PRODUCT_MANAGEMENT_DETAIL_KEY+id;
        //查找缓存
        BackgroundGetProductDetailDVO backgroundGetProductDetailDVO = redissonUtil.getValue(key);
        if (backgroundGetProductDetailDVO != null) {
            return Result.success(backgroundGetProductDetailDVO);
        }
        //查找数据库
        try {
            BackgroundGetProductDetailDVO backgroundGetProductDetailDVO1 = productMapper.selectProductDetailById(id);
            if (backgroundGetProductDetailDVO1 != null) {
                //增加浏览量
                backgroundGetProductDetailDVO1.setViewCount(backgroundGetProductDetailDVO1.getViewCount() + 1);
                //更新数据库
                QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("id", id);
                Product product = new Product();
                product.setViewCount(backgroundGetProductDetailDVO1.getViewCount());
                productMapper.update(product, queryWrapper);
                //设置缓存
                redissonUtil.setValue(key, backgroundGetProductDetailDVO1);
                return Result.success(backgroundGetProductDetailDVO1);
            }
            return Result.error("商品不存在");

        } catch (Exception e) {
            log.error("查询商品详情失败", e);
            return Result.error("查询商品详情失败");
        }
    }

    @Override
    /**
     * 根据条件查询商品信息
     * @param queryProductByConditionDTO 查询条件参数对象，包含分页信息和查询条件
     * @return 返回查询结果，包含商品分页数据
     */
    public Result<PageResult<QueryProductDVO>> queryProductByCondition(QueryProductByConditionDTO queryProductByConditionDTO) {
        //查询数据库
        try {
            // 创建分页对象，设置页码和每页大小
            Page<QueryProductDVO> page = new Page<>(queryProductByConditionDTO.getPageNum(), queryProductByConditionDTO.getPageSize());
            // 根据条件查询商品信息
            List<QueryProductDVO> queryProductDVOList = productMapper.queryProductByCondition(page, queryProductByConditionDTO);
            // 判断查询结果是否为空
            if(queryProductDVOList.isEmpty()){
                throw new UserProductConditionQueryIsEmptyException("查询结果为空");
            }

            // 创建分页结果对象
            PageResult<QueryProductDVO> pageResult = new PageResult<>();
            // 设置查询结果数据
            pageResult.setRows(queryProductDVOList);
            // 设置总记录数
            pageResult.setTotal(Integer.toUnsignedLong(queryProductDVOList.size()));
            // 返回成功结果
            return Result.success(pageResult);

        }catch (UserProductConditionQueryIsEmptyException e){
            log.info("查询商品为空", e);
            return Result.success(e.getMessage());
        }
        catch (Exception e){
            log.error("查询商品失败", e);
            return Result.error("查询商品失败");
        }

    }
}
