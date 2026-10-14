# qi-suo 后端

Spring Cloud 多模块工程（Spring Boot 3.3.4 / Spring Cloud 2023.0.3 / Spring Cloud Alibaba 2023.0.3.2 / MyBatis-Plus 3.5.9）。

## 一、模块与服务

| 服务 | 模块 | 端口 | 网关前缀 | 职责 |
|---|---|---|---|---|
| 网关 | `qi-suo-gateway` | 8080 | — | 路由转发；解析 JWT 并向业务服务注入 `Auth-claims` 头 |
| 认证服务 | `qi-suo-authService` | 8082 | `/api/auth/**` | 登录、验证码、刷新 token、**注册** |
| 用户服务 | `qi-suo-userService` | 8085 | `/api/user/**` | 购物车、订单、商品浏览、个人信息、首页图 |
| 商家服务 | `qi-suo-merchantService` | 8084 | `/api/merchant/**` | 商家商品、订单、店铺信息 |
| 管理服务 | `qi-suo-adminService` | 8081 | `/api/admin/**` | 管理员、商品审核、分类与属性（读+写）、商家与用户管理 |
| 文件服务 | `qi-suo-fileService` | 8083 | `/api/file/**` | 对象存储上传、**图片元数据/轮播图/主页背景图管理** |

公共模块：`qi-suo-pojo`（实体/DTO/DVO）、`qi-suo-common-core`、`qi-suo-common-security`、`qi-suo-common-mybatis`、`qi-suo-common-redisson`、`qi-suo-common-minio`、`qi-suo-common-feign`、`qi-suo-common-log`。

## 二、接口归属

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
| `product`、`product_category`、`product_attribute_value` | **商家服务** `qi-suo-merchant` | 管理服务的商品查询、用户服务的商品浏览、用户服务里改 `is_hot` 的定时任务 |
| `orders`、`order_item`、`cart`、`user_address` | **用户服务** `qi-suo-user` | 商家服务的订单列表/详情/发货 |
| `user`（信息维护） | **用户服务** `qi-suo-user` | 管理服务的用户列表/状态 |
| `merchant`（店铺信息） | **商家服务** `qi-suo-merchant` | 管理服务的商家列表/状态；用户服务取商家名 |
| `admin` | **管理服务** `qi-suo-admin` | 文件服务取图片审计用的操作人 |
| `category`、`attribute`、`attribute_value` | **管理服务** `qi-suo-admin` | 无（用户服务已走 Feign） |
| `carousel_image`、`home_background_image` | **文件服务** `qi-suo-file` | 无（用户服务已走 Feign） |

> 账户入口是明确例外：注册/登录仍由认证服务直接读写 `user`/`merchant` 表。
## 三、网关路由

`qi-suo-gateway/src/main/resources/application.yml` 只有 5 条按前缀划分的路由，与服务一一对应，**顺序不再敏感**：

```yaml
- id: auth-route      # Path=/api/auth/**      → lb://qi-suo-auth
- id: user-route      # Path=/api/user/**      → lb://qi-suo-user
- id: admin-route     # Path=/api/admin/**     → lb://qi-suo-admin
- id: merchant-route  # Path=/api/merchant/**  → lb://qi-suo-merchant
- id: file-route      # Path=/api/file/**      → lb://qi-suo-file
```

> 此前为兼容"前缀 ≠ 归属服务"，这几条路由之外还挂过 4 条特例路由（`user-register-route`、`admin-image-route`、`merchant-category-attribute-route`、`merchant-register-route`），且必须排在通配路由之前。URL 前缀统一后已全部删除。

## 四、服务间调用（Feign）

所有跨服务调用都集中在 `qi-suo-common-feign`，每个客户端配一个降级类（`com.cyj.back.*FallBack`）：

| 客户端（`contextId`） | 方向 | 调用的接口 |
|---|---|---|
| `FileImageFeignClient`（未设 `contextId`，配置键=服务名 `qi-suo-file`） | 用户 → 文件 | `GET /api/file/image/getCarouselImageList`、`GET /api/file/image/getNowHomeBackGroundImage` |
| `AdminCategoryFeignClient`（未设 `contextId`，配置键=服务名 `qi-suo-admin`） | 用户 → 管理 | `GET /api/admin/CategoryAndAttribute/getHomeCategory`（首页分类树） |
| `MerchantProductFeignClient`（`merchantProductFeignClient`） | 用户 → 商家 | `GET /api/merchant/product/queryHotProduct`、`getProductDetail`、`POST /queryProductByCondition` |
| `MerchantProductQueryFeignClient`（`merchantProductQueryFeignClient`） | 管理 → 商家 | `GET /api/merchant/product/queryProductList`、`getProductDetail/{id}` |
| `UserOrdersFeignClient`（`userOrdersFeignClient`） | 商家 → 用户 | `GET /api/user/orders/merchantList`、`detail`、`setStatus` |
| `UserManagementFeignClient`（`userManagementFeignClient`） | 管理 → 用户 | `POST /api/user/userManagement/list`、`PUT /status` |
| `MerchantManagementFeignClient`（`merchantManagementFeignClient`） | 管理 → 商家；用户 → 商家 | `POST /api/merchant/merchantManagement/list`、`PUT /status`、`GET /getMerchantNameById` |
| `AdminNameFeignClient`（`adminNameFeignClient`） | 文件 → 管理 | `GET /api/admin/adminManagement/getAdminNameById`（图片审计字段的操作人） |

`FileFeign`（原有：管理服务 → 文件服务的上传调用）在图片管理并入文件服务后已无调用方，作为遗留代码保留在 `qi-suo-common-feign` 中。

> 服务间调用**不经网关**，所以不会带上网关注入的 `Auth-claims` 头。`com.cyj.interceptors.AuthFeignClientInterceptor` 会为 Feign 请求注入 `Auth-claims`（固定身份 `id=-1`、`roles=["system"]`），被调方据此通过 `anyRequest().authenticated()`——新增跨服务调用无需加白名单。注意它对**所有服务、所有 Feign 客户端**生效，且注入的是固定身份而非真实调用者，所以**凡是"按当前登录者"取数据的接口都不能直接给对方用**，必须把 `userId`/`merchantId`/`adminId` 作为参数显式传过去（如 `getMerchantNameById?id=`、`getAdminNameById?id=`）。

> **降级类要生效，调用方服务必须开 `feign.sentinel.enabled`**（默认是 false，此时 `fallback` 类根本不参与，调用失败会直接抛异常）。管理/用户服务的 `application-feign.yml` 里本来就有；商家/文件服务是本次补上的，并已把 `feign` 加进 `spring.profiles.include`。

> **注意**：同一个服务名被多个 `@FeignClient` 引用时，必须给每个客户端指定唯一的 `contextId`，否则启动即报 `BeanDefinitionOverrideException: ... 'qi-suo-<服务名>.FeignClientSpecification'`（5 个服务全部起不来）。`spring.cloud.openfeign.client.config.<key>` 里的 `<key>` 取的就是 `contextId`（未指定时等于服务名）。

> 写接口的客户端（如 `MerchantManagementFeignClient`）不要配 `retryer: feign.Retryer.Default`：重试会把一次写变成多次写，而且被调方停机时响应会拖到十几秒。

## 五、构建与运行

Maven 未加入 PATH，请使用本机缓存的发行版；各服务打出来的是普通 thin jar（无 `Main-Class`，未做 repackage），**不能用 `java -jar` 启动**：

```bash
MVN=/c/Users/<你>/.m2/wrapper/dists/apache-maven-3.9.11-bin/*/apache-maven-3.9.11/bin/mvn.cmd

"$MVN" -DskipTests clean install
"$MVN" -pl qi-suo-service/qi-suo-adminService dependency:build-classpath -Dmdep.outputFile=cp.txt
java -cp "qi-suo-service/qi-suo-adminService/target/classes;$(cat cp.txt)" com.cyj.AdminServiceApplication
```

- 各服务通过 `application.yml` 的 `spring.profiles.include` 加载附加配置：`mybatis`（数据源 + MyBatis-Plus）、`minio`（文件服务）、`feign`（管理/用户/商家/文件服务）、`mq`（商家/用户服务）。
- 本地依赖的中间件：MySQL 3306、Redis 6379、MinIO 9000、Nacos 8848、RabbitMQ 5672。Nacos 未启动时可用 `--spring.cloud.nacos.discovery.enabled=false` 单独启动某个服务做自检（此时网关无法转发）。
- 数据库脚本未随仓库提供，需自备 `qi-suo` 库。
