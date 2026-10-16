# 架构与实现说明

这里收录 README 之外的实现细节：接口与数据归属、网关路由、服务间调用、订单超时链路。

日常开发看 README 即可；**改动跨服务接口、数据归属，或订单超时相关代码之前，建议先过一遍本文件。**

## 一、接口归属

接口经过一次归属调整，**URL 前缀现在就等于服务归属**，按前缀即可判断提供方：

| 前缀 | 服务 |
|---|---|
| `/api/auth/**` | 认证服务 `qi-suo-auth` |
| `/api/user/**` | 用户服务 `qi-suo-user` |
| `/api/merchant/**` | 商家服务 `qi-suo-merchant` |
| `/api/admin/**` | 管理服务 `qi-suo-admin` |
| `/api/file/**` | 文件服务 `qi-suo-file` |

几个跨域功能的最终归属（前缀与归属一致，无需特例）：

| 接口路径 | 由哪个服务提供 | 说明 |
|---|---|---|
| `/api/auth/user/register`、`/api/auth/merchant/register` | **认证服务** `qi-suo-auth` | 注册与登录、验证码同属账户入口闭环，故挂在 `/api/auth/{user,merchant}/` 之下，与 `/api/auth/{user,merchant}/login` 对称 |
| `/api/admin/CategoryAndAttribute/**` | **管理服务** `qi-suo-admin` | 分类/属性的读接口，与 `/api/admin/category/**` 的写接口同属一个域 |
| `/api/file/image/**` | **文件服务** `qi-suo-file` | 图片管理（轮播图、主页背景图）与对象存储上传同属媒体资源域 |

数据表的唯一归属（跨服务域，越界方已改为服务间调用）：

| 数据域 | 唯一归属 | 原来的越界方（现已改走 Feign） |
|---|---|---|
| `product`、`product_category`、`product_attribute_value` | **商家服务** `qi-suo-merchant` | 管理服务的商品查询、用户服务的商品浏览、用户服务里改 `is_hot` 的定时任务、用户服务的下单扣减与超时归还库存 |
| `orders`、`order_item`、`cart`、`user_address` | **用户服务** `qi-suo-user` | 商家服务的订单列表/详情/发货 |
| `user`（信息维护） | **用户服务** `qi-suo-user` | 管理服务的用户列表/状态 |
| `merchant`（店铺信息） | **商家服务** `qi-suo-merchant` | 管理服务的商家列表/状态；用户服务取商家名 |
| `admin` | **管理服务** `qi-suo-admin` | 文件服务取图片审计用的操作人 |
| `category`、`attribute`、`attribute_value` | **管理服务** `qi-suo-admin` | 无（用户服务已走 Feign） |
| `carousel_image`、`home_background_image` | **文件服务** `qi-suo-file` | 无（用户服务已走 Feign） |

> 账户入口是明确例外：注册/登录仍由认证服务直接读写 `user`/`merchant` 表。

## 二、网关路由

`qi-suo-gateway/src/main/resources/application.yml` 只有 5 条按前缀划分的路由，与服务一一对应，**顺序不再敏感**：

```yaml
- id: auth-route      # Path=/api/auth/**      → lb://qi-suo-auth
- id: user-route      # Path=/api/user/**      → lb://qi-suo-user
- id: admin-route     # Path=/api/admin/**     → lb://qi-suo-admin
- id: merchant-route  # Path=/api/merchant/**  → lb://qi-suo-merchant
- id: file-route      # Path=/api/file/**      → lb://qi-suo-file
```

> 此前为兼容「前缀 ≠ 归属服务」，这几条路由之外还挂过 4 条特例路由（`user-register-route`、`admin-image-route`、`merchant-category-attribute-route`、`merchant-register-route`），且必须排在通配路由之前。URL 前缀统一后已全部删除。

## 三、服务间调用（Feign）

所有跨服务调用都集中在 `qi-suo-common-feign`，每个客户端配一个降级类（`com.cyj.back.*FallBack`）：

| 客户端（`contextId`） | 方向 | 调用的接口 |
|---|---|---|
| `FileImageFeignClient`（未设 `contextId`，配置键=服务名 `qi-suo-file`） | 用户 → 文件 | `GET /api/file/image/getCarouselImageList`、`GET /api/file/image/getNowHomeBackGroundImage` |
| `AdminCategoryFeignClient`（未设 `contextId`，配置键=服务名 `qi-suo-admin`） | 用户 → 管理 | `GET /api/admin/CategoryAndAttribute/getHomeCategory`（首页分类树） |
| `MerchantProductFeignClient`（`merchantProductFeignClient`） | 用户 → 商家 | `GET /api/merchant/product/queryHotProduct`、`getProductDetail`、`POST /queryProductByCondition` |
| `MerchantProductQueryFeignClient`（`merchantProductQueryFeignClient`） | 管理 → 商家 | `GET /api/merchant/product/queryProductList`、`getProductDetail/{id}` |
| `MerchantProductStockFeignClient`（`merchantProductStockFeignClient`） | 用户 → 商家 | `POST /api/merchant/product/deductStock`、`restoreStock`（下单扣减、超时取消归还，见第四节） |
| `UserOrdersFeignClient`（`userOrdersFeignClient`） | 商家 → 用户 | `GET /api/user/orders/merchantList`、`detail`、`setStatus` |
| `UserManagementFeignClient`（`userManagementFeignClient`） | 管理 → 用户 | `POST /api/user/userManagement/list`、`PUT /status` |
| `MerchantManagementFeignClient`（`merchantManagementFeignClient`） | 管理 → 商家；用户 → 商家 | `POST /api/merchant/merchantManagement/list`、`PUT /status`、`GET /getMerchantNameById` |
| `AdminNameFeignClient`（`adminNameFeignClient`） | 文件 → 管理 | `GET /api/admin/adminManagement/getAdminNameById`（图片审计字段的操作人） |

`FileFeign`（原有：管理服务 → 文件服务的上传调用）在图片管理并入文件服务后已无调用方，作为遗留代码保留在 `qi-suo-common-feign` 中。

> 服务间调用**不经网关**，所以不会带上网关注入的 `Auth-claims` 头。`com.cyj.interceptors.AuthFeignClientInterceptor` 会为 Feign 请求注入 `Auth-claims`（固定身份 `id=-1`、`roles=["system"]`），被调方据此通过 `anyRequest().authenticated()` —— 新增跨服务调用无需加白名单。注意它对**所有服务、所有 Feign 客户端**生效，且注入的是固定身份而非真实调用者，所以**凡是「按当前登录者」取数据的接口都不能直接给对方用**，必须把 `userId`/`merchantId`/`adminId` 作为参数显式传过去（如 `getMerchantNameById?id=`、`getAdminNameById?id=`）。

> **降级类要生效，调用方服务必须开 `feign.sentinel.enabled`**（默认是 false，此时 `fallback` 类根本不参与，调用失败会直接抛异常；返回的是 `Result.error(...)` 而不是抛异常）。管理/用户服务的 `application-feign.yml` 里本来就有；商家/文件服务是后来补上的，并已把 `feign` 加进 `spring.profiles.include`。

> **注意**：同一个服务名被多个 `@FeignClient` 引用时，必须给每个客户端指定唯一的 `contextId`，否则启动即报 `BeanDefinitionOverrideException: ... 'qi-suo-<服务名>.FeignClientSpecification'`（5 个服务全部起不来）。`spring.cloud.openfeign.client.config.<key>` 里的 `<key>` 取的就是 `contextId`（未指定时等于服务名）。

> 写接口的客户端（如 `MerchantManagementFeignClient`）不要配 `retryer: feign.Retryer.Default`：重试会把一次写变成多次写，而且被调方停机时响应会拖到十几秒。

## 四、订单超时未支付自动取消

下单后 `qi-suo.order.timeout`（默认 15 分钟）内未支付，订单自动置为「已取消」并归还占用的库存。全部代码在 **用户服务**（生产者、消费者、兜底扫描都在这一个服务里）。

### 4.1 链路

```
POST /api/user/cart/createOrder  (@Transactional)
  1. 读 Redis 购物车 → 校验地址存在 → Feign 取商家名     ← 前置校验，失败即返回，不写任何东西
  2. INSERT orders(status='未支付', stock_state=1) + INSERT order_item
  3. Feign POST /api/merchant/product/deductStock        ← 失败则 setRollbackOnly + Result.error，订单不落库
  4. afterCommit: 清 Redis 购物车缓存 → 投递延迟消息
     afterCompletion(ROLLED_BACK): Feign restoreStock 补偿（扣减已成功但订单被回滚时）

qi-suo.order.delay.queue  (x-message-ttl，无消费者)
        │ 到期 → 死信
        ▼
qi-suo.order.cancel.queue → OrderTimeoutConsumer ──┐
@Scheduled(60s) OrderTimeoutSweepJob ─────────────┤→ cancelUnpaidOrder(orderId)
     · 扫 status='未支付' AND created_at<=NOW()-timeout
     · 补偿扫 status='已取消' AND stock_state=1 AND updated_at<=NOW()-60s
```

`cancelUnpaidOrder` 的第一步是一条 CAS：`UPDATE orders SET status='已取消' WHERE id=? AND status='未支付'`。**影响行数 = 1 的那一方独占后续动作**，支付、发货、MQ 消费者、多个实例的扫描全部收敛到这一条语句上，因此不需要分布式锁，库存也不会被归还两次。

### 4.2 `orders.stock_state`

| 值 | 含义 |
|---|---|
| `0` | 本单未占用库存（**存量订单**默认值，无需数据迁移） |
| `1` | 已占用，待归还 |
| `2` | 已归还 |

`affectedRows==1` 只保证「取消」只有一个赢家，保证不了「归还」一定成功（CAS 提交后、Feign 返回前可能宕机）。所以归还成功后再把 `stock_state` 从 1 CAS 到 2，交给补偿扫描重试 `1 → 2` 的那部分。

### 4.3 配置（`qi-suo-userService/src/main/resources/application.yml`）

| 配置项 | 默认 | 说明 |
|---|---|---|
| `qi-suo.order.timeout` | `15m` | 支持 `15m`/`900s`/`PT15M` |
| `qi-suo.order.timeout-consumer-enabled` | `true` | 关掉只留扫描（排障用） |
| `qi-suo.order.sweep-enabled` | `true` | 兜底扫描开关 |
| `qi-suo.order.sweep-interval-ms` | `60000` | 扫描间隔 |
| `qi-suo.order.sweep-batch-size` | `200` | 单轮最多处理多少单 |
| `qi-suo.order.consumer-concurrency` / `consumer-prefetch` | `1` / `1` | 消费者并发与预取 |

### 4.4 运维须知

- **`x-message-ttl` 是队列声明参数，改 `timeout` 后直接重启不会生效**，只会打 `PRECONDITION_FAILED`（而且同一 channel 上的 binding 声明会连带失败）。改超时值的流程：停 userService → 删 `qi-suo.order.delay.queue`（队列里待处理的消息一并丢失）→ 改配置 → 重启。丢失的订单由扫描任务兜底。
- 队列拓扑声明在用户服务本地（`config/MqConfig.java`），**不要**挪进 `qi-suo-common-rabbitMQ`：那个模块被 5 个服务传递依赖，且 `x-message-ttl` 是可配置项，多个服务各自 declare 会因参数不一致互相打架。
- 不要给单条消息设 `expiration` 来实现延迟：TTL 不一致时队头消息未过期会阻塞后面的消息（队头阻塞）。队列级 TTL 没有这个问题（本场景所有消息 TTL 一致）。
- 清理一条卡住的订单：Redis 里删 `user:cart:{userId}:{merchantId}`，DB 里对 `orders`/`order_item` 按 `order_id` 删即可；`stock_state=1` 的单删除前记得手工归还库存。

### 4.5 已知残余风险

- **归还库存不是幂等的**：Feign 读超时（10s）但商家侧其实已执行成功时，`stock_state` 仍是 1，下轮补偿扫描会重复归还，表现为库存虚增。彻底关掉这个窗口需要商家侧加一张 `order_stock_restore(order_id)` 幂等表，**尚未做**。
- **扣减成功但订单没落库**：进程在扣减成功与提交之间崩溃时，这笔占用没有任何订单行可见，扫描也看不到。概率极低，需要时用对账 SQL 比对 `product.stock` 与 `SUM(order_item.quantity)`。
