package com.cyj.service.Impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.constant.RedissonKey;
import com.cyj.dto.BackgroundGetProductByConditionDTO;
import com.cyj.dvo.BackgroundGetProductDVO;
import com.cyj.dvo.BackgroundGetProductDetailDVO;
import com.cyj.mapper.ProductMapper;
import com.cyj.service.ProductManagementService;
import com.cyj.utils.RedissonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductManagementServiceImpl implements ProductManagementService {

    @Autowired
    private RedissonUtil redissonUtil;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public Result<PageResult<BackgroundGetProductDVO>> getProduct(BackgroundGetProductByConditionDTO backgroundGetProductByConditionDTO) {
        if(backgroundGetProductByConditionDTO ==null){
            return Result.error("错误查询 null");
        }

        if(backgroundGetProductByConditionDTO.getPageNum() == null
                || backgroundGetProductByConditionDTO.getPageSize() == null){
            return Result.error("页码为空");
        }
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
}
