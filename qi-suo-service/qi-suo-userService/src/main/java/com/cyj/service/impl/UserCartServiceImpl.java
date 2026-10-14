package com.cyj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cyj.common.OrdersProductShow;
import com.cyj.common.Result;
import com.cyj.constant.RedissonKey;
import com.cyj.dto.CreateOrderDTO;
import com.cyj.dvo.CartDVO;
import com.cyj.dvo.UserOrdersDetailDVO;
import com.cyj.entity.*;
import com.cyj.exception.ProductIsNotExistException;
import com.cyj.exception.ProductStockIsNotException;
import com.cyj.feign.merchant.MerchantManagementFeignClient;
import com.cyj.mapper.*;
import com.cyj.service.UserCartService;
import com.cyj.utils.RedissonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Service
@Slf4j
public class UserCartServiceImpl implements UserCartService {

    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private UserAddressMapper userAddressMapper;
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private MerchantManagementFeignClient merchantManagementFeignClient;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private RedissonUtil redissonUtil;
    /**
     * 添加购物车项的方法
     *
     * @param productId  商品ID，用于标识要添加的商品
     * @param quantity   商品数量，表示要添加的商品数量
     */
    @Override
    public Result<String> addCartItem(Long productId,  Integer quantity) {

        Long userid = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("当前用户ID：{}, 添加购物车项，商品ID：{}，数量：{}",userid, productId, quantity);
        //从数据库查询商品数据
        try {
            Product product = productMapper.selectById(productId);
            //判断商品是否存在
            if(product == null){
                throw new ProductIsNotExistException("商品不存在");
            }
            //判断库存
            if(product.getStock() < quantity){
                throw new ProductStockIsNotException("商品库存不足");
            }

            //将商品数据添加到购物车中
            Cart cart = new Cart();
            cart.setUserId(userid);
            cart.setProductId(productId);
            cart.setMerchantId(product.getMerchantId());
            cart.setQuantity(quantity);
            cart.setPrice(product.getPrice());
            cart.setTotalPrice(new BigDecimal(quantity).multiply(product.getPrice()));
            Cart c = cartMapper.selectOne(new QueryWrapper<Cart>().eq("user_id", userid).eq("product_id", productId));
            if(c == null){
                cartMapper.insert(cart);
            }else {
                c.setQuantity(c.getQuantity() + 1);
                c.setTotalPrice(new BigDecimal(c.getQuantity()).multiply(product.getPrice()));
                cartMapper.updateById(c);
            }
            //将购物车数据存储到Redis中
            String cartKey = RedissonKey.USER_CART_KEY + userid + ":" + product.getMerchantId();
            List<Cart> cartList = redissonUtil.getList(cartKey);
            //存在判断是否已经存在此商品
            if (cartList != null && !cartList.isEmpty() ){
                for (int i = 0; i < cartList.size(); i++) {
                    Cart cart1 = cartList.get(i);
                    if (Objects.equals(cart1.getProductId(), productId)) {
                        cart1.setQuantity(cart1.getQuantity() + quantity);
                        cart1.setTotalPrice(new BigDecimal(cart1.getQuantity()).multiply(product.getPrice()));
                        //修改数据库
                        cartMapper.updateById(cart1);
                        //修改缓存
                        cartList.set(i, cart1);
                        return Result.success("添加购物车项成功");
                    }
                }
                //不存在直接添加
                cartList.add(cart);
            }else {
                //购物车不存在，创建缓存
                List<Cart> carts = new ArrayList<>();
                carts.add(cart);
                redissonUtil.addAllToList(cartKey, carts);
            }


            return Result.success("添加购物车项成功");
        }catch (Exception e){
            log.error("添加购物车项失败：{}", e.getMessage());
            return Result.error("添加购物车项失败");
        }
    }

    @Override    // 重写父类方法的注解

    /**
     * 删除购物车中指定商品的接口方法
     * @param productId 商品ID，用于标识要删除的商品
     * @return 返回一个Result对象，其中包含操作结果信息
     */
    @Transactional
    public Result<String> deleteCartItem(Long productId) {
        Long userid = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("当前用户ID：{}, 删除购物车项，商品ID：{}",userid, productId);
        try {
            Product product = productMapper.selectById(productId);
            //查询缓存
            String cartKey = RedissonKey.USER_CART_KEY + userid + ":" + product.getMerchantId();
            List<Cart> cartList = redissonUtil.getList(cartKey);
            int i = 0;
            for(Cart cart : cartList){
                if(Objects.equals(cart.getProductId(), productId)){
                    //恢复商品库存

                    product.setStock(product.getStock() + 1);
                    productMapper.updateById(product);
                    //修改缓存
                    cart.setQuantity(cart.getQuantity() - 1);
                    if(cart.getQuantity() <= 0){
                        cartList.remove(i);
                        cartMapper.deleteById(cart);
                        break;
                    }
                    cartList.set(i,cart);
                    cartMapper.updateById(cart);

                    break;
                }
                i++;
            }

            return Result.success();
        }catch (Exception e){
            log.error("删除购物车项失败：{}", e.getMessage());
            throw new RuntimeException("删除购物车项失败");
        }
    }

    @Override
    public Result<List<CartDVO>> getCartList(Long merchantId) {
        //查询缓存
        Long userid = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String cartKey = RedissonKey.USER_CART_KEY + userid + ":" + merchantId;
        List<Cart> cartList = redissonUtil.getList(cartKey);
        if(cartList == null){
            return Result.success("购物车为空");
        }
        List<CartDVO> cartDVOList = new ArrayList<>();
        for(Cart cart : cartList){
            Product product = productMapper.selectById(cart.getProductId());
            CartDVO cartDVO =  CartDVO.newCartDVO(cart,product);
            cartDVOList.add(cartDVO);
        }
        return Result.success(cartDVOList);
    }

    @Override
    @Transactional
    public Result<UserOrdersDetailDVO> createOrder(CreateOrderDTO createOrderDTO) {

        //创建返回值
        UserOrdersDetailDVO userOrdersDetailDVO = new UserOrdersDetailDVO();
        //获取用户id
        Long userid = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("当前用户ID：{}, 购物车结算生成订单",userid);
        //获取商家id
        Long sellerId = createOrderDTO.getMerchantId();
        //查询缓存获取购物车列表
        String cartKey = RedissonKey.USER_CART_KEY + userid+":"+sellerId;
        List<Cart> cartList = redissonUtil.getList(cartKey);
        if(cartList == null){
            return Result.error("购物车为空");
        }
        //获取地址
        Long addressId = createOrderDTO.getUserAddressId();
        //创建订单号
        String orderNo = UUID.randomUUID().toString().replace("-", "");
        //计算总价
        BigDecimal totalPrice = new BigDecimal(0);
        for(Cart cart : cartList){
            totalPrice = totalPrice.add(cart.getTotalPrice());
        }


        //生成订单
        Orders orders =  new Orders();
        orders.setStatus("待支付");
        orders.setUserId(userid);
        orders.setMerchantId(sellerId);
        orders.setOrderNo(orderNo);
        orders.setUserAddressId(addressId);
        orders.setTotalAmount(totalPrice);
        ordersMapper.insert(orders);

        //生成订单项
        for(Cart cart : cartList){
            OrderItem orderItem = new OrderItem();
            orderItem.setTotalPrice(cart.getTotalPrice());
            orderItem.setOrderId(orders.getId());
            orderItem.setPrice(cart.getPrice());
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setProductId(cart.getProductId());
            //插入数据库
            orderItemMapper.insert(orderItem);
        }

        //生成返回数据（订单详情）
        userOrdersDetailDVO.setCreatedAt(orders.getCreatedAt());
        userOrdersDetailDVO.setOrderNo(orders.getOrderNo());
        userOrdersDetailDVO.setTotalAmount(orders.getTotalAmount());
        //获取地址详情
        UserAddress userAddress = userAddressMapper.selectById(addressId);
        userOrdersDetailDVO.setDetailAddress(userAddress.getDetailAddress());
        userOrdersDetailDVO.setUserAddressId(userAddress.getId());
        //获取商家名（商家信息由商家服务提供）
        Result<String> merchantNameResult = merchantManagementFeignClient.getMerchantNameById(sellerId);
        if(merchantNameResult.getCode() == 0){
            //与原先查不到商家时一致：抛异常让订单事务回滚
            throw new RuntimeException("获取商家信息失败");
        }
        userOrdersDetailDVO.setMerchantName(merchantNameResult.getData());
        userOrdersDetailDVO.setStatus(orders.getStatus());
        userOrdersDetailDVO.setMerchantId(sellerId);
        //生成订单显示处商品列表
        List<OrdersProductShow> productList = new ArrayList<>();
        for(Cart cart : cartList){
            Product product = productMapper.selectById(cart.getProductId());
            OrdersProductShow ordersProductShow = new OrdersProductShow();
            ordersProductShow.setProductId(product.getId());
            ordersProductShow.setProductName(product.getProductName());
            ordersProductShow.setPrice(product.getPrice());
            ordersProductShow.setQuantity(cart.getQuantity());
            ordersProductShow.setTotalPrice(cart.getTotalPrice());
            ordersProductShow.setProductImageUrl(product.getProductImageUrl());
            productList.add(ordersProductShow);
        }
        userOrdersDetailDVO.setProductList(productList);

        //清空购物车
        //清空缓存
        redissonUtil.delete(cartKey);
        //清空数据库


       return Result.success(userOrdersDetailDVO);
    }
}
