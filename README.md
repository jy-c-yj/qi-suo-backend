# 栖所 qi-suo · 后端

家居电商方向的 Spring Cloud 微服务后端。一个网关统一入口，按角色承载**用户端 / 商家端 / 管理端**三端；服务间通过 Feign 协作，跨服务的数据各归其主（归属表见 [docs/architecture.md](docs/architecture.md)）。

前端（Vue 3 + Vite + Element Plus）在独立仓库。

## 技术栈

| 类别 | 选型 |
|---|---|
| 语言 / 构建 | Java 17 · Maven 多模块 |
| 框架 | Spring Boot 3.3.4 · Spring Cloud 2023.0.3 · Spring Cloud Alibaba 2023.0.3.2 |
| 注册 / 网关 | Nacos · Spring Cloud Gateway |
| 安全 | Spring Security + JWT（jjwt 0.11.5）、kaptcha 验证码 |
| 持久层 | MyBatis-Plus 3.5.9 · Druid · MySQL 8 |
| 缓存 | Redis（Redisson 3.25） |
| 消息 | RabbitMQ（订单超时、接口日志落库） |
| 对象存储 | MinIO 8.6 |
| 服务调用 | OpenFeign + Sentinel 降级 |

## 模块与服务

| 服务 | 模块 | 端口 | 网关前缀 | 职责 |
|---|---|---|---|---|
| 网关 | `qi-suo-gateway` | 8080 | — | 路由转发；校验 JWT 并向下游注入 `Auth-claims` |
| 认证服务 | `qi-suo-service/qi-suo-authService` | 8082 | `/api/auth/**` | 登录、注册、验证码、刷新令牌 |
| 用户服务 | `qi-suo-service/qi-suo-userService` | 8085 | `/api/user/**` | 购物车、订单、商品浏览、个人信息、首页图 |
| 商家服务 | `qi-suo-service/qi-suo-merchantService` | 8084 | `/api/merchant/**` | 商家商品、订单、店铺信息 |
| 管理服务 | `qi-suo-service/qi-suo-adminService` | 8081 | `/api/admin/**` | 管理员、商品审核、分类与属性、商家与用户管理 |
| 文件服务 | `qi-suo-service/qi-suo-fileService` | 8083 | `/api/file/**` | 对象存储上传、轮播图与主页背景图管理 |

公共模块 `qi-suo-common/`：`common-core`、`common-security`、`common-mybatis`、`common-redisson`、`common-minio`、`common-feign`、`common-rabbitMQ`；实体与 DTO/DVO 在 `qi-suo-pojo`。

系统模块 `qi-suo-sys/`：`qi-suo-sys-log` —— 接口请求日志（AOP 采集、`traceId` 贯穿、RabbitMQ 批量落库）。

## 环境依赖

| 组件 | 默认地址 | 用途 |
|---|---|---|
| MySQL | `127.0.0.1:3306` | 业务库 `qi-suo` |
| Redis | `127.0.0.1:6379` | 缓存、购物车、刷新令牌 |
| Nacos | `127.0.0.1:8848` | 服务注册 |
| RabbitMQ | `127.0.0.1:5672` | 订单超时、日志 |
| MinIO | `127.0.0.1:9000` | 图片对象存储 |

建库脚本 docs/sql/：`qi-suo.sql`

## 构建与运行
| 模块 | 启动类 |
|---|---|
| `qi-suo-gateway` | `com.cyj.GatewayApplication` |
| `qi-suo-adminService` | `com.cyj.AdminServiceApplication` |
| `qi-suo-authService` | `com.cyj.AuthServiceApplication` |
| `qi-suo-fileService` | `com.cyj.FileServiceApplication` |
| `qi-suo-merchantService` | `com.cyj.MerchantServiceApplication` |
| `qi-suo-userService` | `com.cyj.UserServiceApplication` |

只调试单个服务时，可加 `--spring.cloud.nacos.discovery.enabled=false` 跳过注册（此时网关无法转发到它）。

## 关键约定

- **统一响应体**：`Result<T>`（`code=1` 成功、`0` 失败，说明文字在 `msg`；注意 `Result.success(String)` 是把值放进 `msg`）与分页 `PageResult<T>`。
- **鉴权链路**：前端带 `Authorization-token` → 网关校验 JWT → 通过后把 `id` 与 `roles` 以 `Auth-claims` 头注入下游 → 各服务用 `@PreAuthorize` 校验。角色字面量**不带 `ROLE_` 前缀**，因此只能用 `hasAuthority` / `hasAnyAuthority`。
- **服务间调用的身份是固定的**：Feign 不经网关，由 `AuthFeignClientInterceptor` 注入 `id=-1`、`roles=["system"]`。所以「按当前登录者取数」的接口不能直接给对方调用，必须把 `userId` / `merchantId` 显式作为参数传过去。
- **URL 前缀即服务归属**（`/api/auth` → 认证，`/api/user` → 用户，以此类推），网关只有 5 条按前缀划分的路由。
- **跨服务调用集中在 `qi-suo-common-feign`**，每个客户端配一个 `com.cyj.back.*FallBack` 降级类；同一个服务名被多个客户端引用时必须指定唯一的 `contextId`；写接口不要配 `retryer`（重试会把一次写变成多次写）。
- **日志**：`@Log` 注解 + AOP 采集接口日志，`traceId` 贯穿网关与服务，异步落 `sys_oper_log`。

## 主要功能

- **账号**：用户 / 商家 / 管理员三端登录、图形验证码、注册、JWT 刷新令牌。
- **商品**：商品与 SKU、分类与属性、上下架、按条件与关键词检索、热门商品。
- **交易**：购物车、下单、支付、订单状态流转，商家发货，管理端与商家端订单视图。
- **订单超时自动取消**：下单 15 分钟未支付自动置为「已取消」并归还库存。用 RabbitMQ 死信队列 + 队列级 TTL 实现延迟，另有 `@Scheduled` 扫描兜底，库存扣减与归还通过 Feign 调到商家服务。实现细节与运维须知见 [docs/architecture.md](docs/architecture.md) 第四节。
- **媒体**：MinIO 对象存储上传，轮播图与主页背景图管理。
- **接口日志**：AOP 采集 + traceId + MQ 批量入库，支持审计查询。


## License

[Apache License 2.0](LICENSE)
