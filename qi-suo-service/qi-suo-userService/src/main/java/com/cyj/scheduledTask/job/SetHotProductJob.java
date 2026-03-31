package com.cyj.scheduledTask.job;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cyj.constant.RedissonKey;
import com.cyj.entity.Product;
import com.cyj.mapper.ProductMapper;
import com.cyj.utils.RedissonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 热门商品定时任务组件
 * 该组件负责定时执行热门商品的设置任务
 */
@Component
public class SetHotProductJob {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private RedissonUtil redissonUtil;

    public void Test(){
        System.out.println("定时任务执行");
    }
    /**
     * 设置热门商品的方法
     * 使用@Scheduled注解实现定时任务，每天凌晨0点执行一次
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void setHotProduct(){

        // 查询数据库，销售量在1以上的设置为热销
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", 0)  // 未删除的商品
                .eq("status", 1)              // 已上架的商品
                .gt("sales", 1);              // 销售量大于1的商品

        // 创建一个用于更新的Product对象，只设置需要更新的字段
        Product updateProduct = new Product();
        updateProduct.setIsHot(true);

        // 执行更新操作
        // 使用 update() 方法，传入要更新的对象和查询条件
        // 这将会把所有满足查询条件的商品的 is_hot 字段更新为 1
        productMapper.update(updateProduct, queryWrapper);
        //删除缓存
        String key = RedissonKey.HOT_PRODUCT_KEY;
        redissonUtil.deleteByPattern(key);
        //更新缓存
    }
}
