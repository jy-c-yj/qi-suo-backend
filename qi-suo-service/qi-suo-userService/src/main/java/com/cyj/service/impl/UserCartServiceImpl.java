package com.cyj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cyj.common.OrderStatus;
import com.cyj.common.OrderStockState;
import com.cyj.common.OrdersProductShow;
import com.cyj.common.Result;
import com.cyj.constant.RedissonKey;
import com.cyj.dto.CreateOrderDTO;
import com.cyj.dto.ProductStockDTO;
import com.cyj.dto.ProductStockItemDTO;
import com.cyj.dvo.CartDVO;
import com.cyj.dvo.CartMerchantGroupDVO;
import com.cyj.dvo.UserOrdersDetailDVO;
import com.cyj.entity.*;
import com.cyj.exception.ProductIsNotExistException;
import com.cyj.exception.ProductStockIsNotException;
import com.cyj.feign.merchant.MerchantManagementFeignClient;
import com.cyj.feign.merchant.MerchantProductStockFeignClient;
import com.cyj.mapper.*;
import com.cyj.mq.OrderTimeoutPublisher;
import com.cyj.service.UserCartService;
import com.cyj.utils.RedissonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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
    private MerchantProductStockFeignClient productStockFeignClient;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private RedissonUtil redissonUtil;
    @Autowired
    private OrderTimeoutPublisher orderTimeoutPublisher;
    /**
     * 添加购物车项的方法
     *
     * @param productId  商品ID，用于标识要添加的商品
     * @param quantity   商品数量，表示要添加的商品数量
     */
    @Override
    public Result<String>
    addCartItem(Long productId,  Integer quantity) {

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
                    //购物车从不持有库存（下单时才扣减），删购物车项不涉及库存
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
    public Result<List<CartMerchantGroupDVO>> getAllCartList() {
        Long userId = currentUserId();

        // DB 行只用来枚举商家：下单成功后 createOrder 只删 Redis、不删 DB，
        // 如果这里回退 DB 明细，已结算的商品会重新出现在购物车里
        List<Cart> dbCarts = cartMapper.selectList(
                new QueryWrapper<Cart>().eq("user_id", userId));
        Set<Long> merchantIds = new LinkedHashSet<>();
        for (Cart cart : dbCarts) {
            if (cart.getMerchantId() != null) {
                merchantIds.add(cart.getMerchantId());
            }
        }

        List<CartMerchantGroupDVO> groups = new ArrayList<>();
        for (Long merchantId : merchantIds) {
            // 明细以 Redis 为准，缓存为空说明购物车已结算或已失效
            List<Cart> redisCarts = redissonUtil.getList(cartKey(userId, merchantId));
            if (redisCarts == null || redisCarts.isEmpty()) {
                continue;
            }

            List<CartDVO> items = new ArrayList<>();
            BigDecimal totalPrice = BigDecimal.ZERO;
            int totalQuantity = 0;
            for (Cart cart : redisCarts) {
                Product product = productMapper.selectById(cart.getProductId());
                if (product == null) {
                    log.warn("购物车中的商品已不存在，跳过展示, userId={}, productId={}",
                            userId, cart.getProductId());
                    continue;
                }
                items.add(CartDVO.newCartDVO(cart, product));
                if (cart.getTotalPrice() != null) {
                    totalPrice = totalPrice.add(cart.getTotalPrice());
                }
                if (cart.getQuantity() != null) {
                    totalQuantity += cart.getQuantity();
                }
            }
            if (items.isEmpty()) {
                continue;
            }

            groups.add(new CartMerchantGroupDVO(
                    merchantId,
                    getMerchantName(merchantId),
                    items,
                    totalPrice,
                    totalQuantity));
        }
        groups.sort(Comparator.comparing(CartMerchantGroupDVO::getMerchantId));
        return Result.success(groups);
    }

    @Override
    @Transactional
    public Result<CartDVO> updateCartItemQuantity(Long productId, Integer quantity) {
        Long userId = currentUserId();
        if (quantity == null || quantity < 1) {
            return Result.error("商品数量必须大于 0");
        }

        Product product = productMapper.selectById(productId);
        if (product == null) {
            return Result.error("商品不存在");
        }
        if (product.getStock() == null || product.getStock() < quantity) {
            return Result.error("商品库存不足");
        }

        Cart dbCart = cartMapper.selectOne(new QueryWrapper<Cart>()
                .eq("user_id", userId)
                .eq("product_id", productId));
        Long merchantId = dbCart != null && dbCart.getMerchantId() != null
                ? dbCart.getMerchantId()
                : product.getMerchantId();

        List<Cart> redisCarts = redissonUtil.getList(cartKey(userId, merchantId));
        if (redisCarts == null || redisCarts.isEmpty()) {
            return Result.error("购物车已失效，请刷新后重试");
        }

        int index = -1;
        for (int i = 0; i < redisCarts.size(); i++) {
            if (Objects.equals(redisCarts.get(i).getProductId(), productId)) {
                index = i;
                break;
            }
        }
        if (index < 0) {
            return Result.error("购物车中不存在该商品");
        }

        Cart cart = redisCarts.get(index);
        cart.setMerchantId(merchantId);
        cart.setPrice(product.getPrice());
        cart.setQuantity(quantity);
        cart.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));

        // 先落库再写缓存：缓存写失败时抛异常让事务回滚，避免两端数量长期不一致
        if (dbCart == null) {
            cart.setId(null);
            cart.setUserId(userId);
            cartMapper.insert(cart);
        } else {
            dbCart.setPrice(cart.getPrice());
            dbCart.setQuantity(cart.getQuantity());
            dbCart.setTotalPrice(cart.getTotalPrice());
            cartMapper.updateById(dbCart);
        }

        try {
            redisCarts.set(index, cart);
        } catch (Exception e) {
            log.error("修改购物车数量写缓存失败, userId={}, productId={}", userId, productId, e);
            throw new RuntimeException("修改购物车数量失败", e);
        }

        return Result.success(CartDVO.newCartDVO(cart, product));
    }

    @Override
    @Transactional
    public Result<String> removeCartItem(Long productId) {
        Long userId = currentUserId();

        Cart dbCart = cartMapper.selectOne(new QueryWrapper<Cart>()
                .eq("user_id", userId)
                .eq("product_id", productId));
        Product product = productMapper.selectById(productId);
        Long merchantId = dbCart != null && dbCart.getMerchantId() != null
                ? dbCart.getMerchantId()
                : (product != null ? product.getMerchantId() : null);

        if (merchantId != null) {
            String key = cartKey(userId, merchantId);
            List<Cart> redisCarts = redissonUtil.getList(key);
            if (redisCarts != null && !redisCarts.isEmpty()) {
                // 从后往前删，避免删除过程中下标错位
                for (int i = redisCarts.size() - 1; i >= 0; i--) {
                    if (Objects.equals(redisCarts.get(i).getProductId(), productId)) {
                        redisCarts.remove(i);
                    }
                }
                if (redisCarts.isEmpty()) {
                    redissonUtil.delete(key);
                }
            }
        }

        if (dbCart != null) {
            cartMapper.deleteById(dbCart.getId());
        }
        // 本来就不存在也返回成功，前端重复点击不会报错
        return Result.success("已移除");
    }

    private Long currentUserId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private String cartKey(Long userId, Long merchantId) {
        return RedissonKey.USER_CART_KEY + userId + ":" + merchantId;
    }

    /**
     * 商家名取不到时用 ID 兜底，不能因为一个 Feign 调用失败让整个购物车列表报错。
     */
    private String getMerchantName(Long merchantId) {
        try {
            Result<String> result = merchantManagementFeignClient.getMerchantNameById(merchantId);
            if (result != null
                    && Integer.valueOf(1).equals(result.getCode())
                    && result.getData() != null
                    && !result.getData().isBlank()) {
                return result.getData();
            }
        } catch (Exception e) {
            log.warn("获取商家名称失败, merchantId={}", merchantId, e);
        }
        return "商家" + merchantId;
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
        UserAddress userAddress = addressId == null ? null : userAddressMapper.selectById(addressId);
        if(userAddress == null){
            return Result.error("收货地址不存在");
        }
        //获取商家名（商家信息由商家服务提供）
        Result<String> merchantNameResult = merchantManagementFeignClient.getMerchantNameById(sellerId);
        if(merchantNameResult == null || !Integer.valueOf(1).equals(merchantNameResult.getCode())){
            return Result.error("获取商家信息失败");
        }
        //创建订单号
        String orderNo = UUID.randomUUID().toString().replace("-", "");
        //计算总价
        BigDecimal totalPrice = new BigDecimal(0);
        for(Cart cart : cartList){
            totalPrice = totalPrice.add(cart.getTotalPrice());
        }


        //生成订单
        Orders orders =  new Orders();
        orders.setStatus(OrderStatus.UNPAID);
        orders.setStockState(OrderStockState.RESERVED); //已占用库存
        orders.setUserId(userid);
        orders.setMerchantId(sellerId);
        orders.setOrderNo(orderNo);
        orders.setUserAddressId(addressId);
        orders.setTotalAmount(totalPrice);
        ordersMapper.insert(orders);

        //生成订单项，顺便收集扣减库存的入参
        List<ProductStockItemDTO> stockItems = new ArrayList<>();
        for(Cart cart : cartList){
            OrderItem orderItem = new OrderItem();
            orderItem.setTotalPrice(cart.getTotalPrice());
            orderItem.setOrderId(orders.getId());
            orderItem.setPrice(cart.getPrice());
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setProductId(cart.getProductId());
            //插入数据库
            orderItemMapper.insert(orderItem);
            stockItems.add(new ProductStockItemDTO(cart.getProductId(), cart.getQuantity()));
        }

        //扣减库存
        Result<String> deductResult;
        try {
            deductResult = productStockFeignClient.deductStock(new ProductStockDTO(orders.getId(), stockItems));
        }catch (Exception e){
            log.error("扣减库存调用异常, orderId={}", orders.getId(), e);
            deductResult = null;
        }
        if(deductResult == null || !Integer.valueOf(1).equals(deductResult.getCode())){
            String message = deductResult == null ? "扣减库存失败，请稍后重试" : deductResult.getMsg();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.error(message);
        }

        //扣减确认成功之后才注册补偿钩子：提交成功永不触发；回滚说明订单没落库，这笔占用必须还回去
        registerStockRestoreOnRollback(orders.getId(), stockItems);

        //生成返回数据（订单详情）
        userOrdersDetailDVO.setCreatedAt(orders.getCreatedAt());
        userOrdersDetailDVO.setOrderNo(orders.getOrderNo());
        userOrdersDetailDVO.setTotalAmount(orders.getTotalAmount());
        userOrdersDetailDVO.setDetailAddress(userAddress.getDetailAddress());
        userOrdersDetailDVO.setUserAddressId(userAddress.getId());
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

        //事务结束后清空购物车缓存 、投递超时延迟消息
        orderTimeoutPublisher.afterCommit(() -> {
            try {
                redissonUtil.delete(cartKey);
            }catch (Exception e){
                log.error("下单成功后清理购物车缓存失败, cartKey={}", cartKey, e);
            }
            orderTimeoutPublisher.publish(orders.getId(), orders.getOrderNo());
        });

       return Result.success(userOrdersDetailDVO);
    }

    /**
     * 注册「事务回滚则归还库存」的补偿钩子——扣减成功之后才调用。
     */
    private void registerStockRestoreOnRollback(Long orderId, List<ProductStockItemDTO> stockItems) {
        if(!TransactionSynchronizationManager.isSynchronizationActive()){
            //当前线程没有事务，无法注册回调，直接返回
            return;
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCompletion(int status) {
                //提交成功，订单入库，直接退出
                if(status != TransactionSynchronization.STATUS_ROLLED_BACK){
                    return;
                }
                try {
                    Result<String> result = productStockFeignClient.restoreStock(new ProductStockDTO(orderId, stockItems));
                    if(result == null || !Integer.valueOf(1).equals(result.getCode())){
                        log.error("下单回滚后归还库存失败, orderId={}, resp={}，需人工核对商品库存", orderId, result);
                    }else {
                        log.info("下单回滚，已归还库存, orderId={}", orderId);
                    }
                }catch (Exception e){
                    log.error("下单回滚后归还库存异常, orderId={}，需人工核对商品库存", orderId, e);
                }
            }
        });
    }
}
