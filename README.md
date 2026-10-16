# 栖所 qi-suo · 后端

家居电商方向的 Spring Cloud 微服务后端。一个网关统一入口，按角色承载**用户端 / 商家端 / 管理端**三端；服务间通过 Feign 协作，跨服务的数据各归其主。

前端（Vue 3 + Vite + Element Plus）在独立仓库[**qi-suo-front**](https://github.com/jy-c-yj/qi-suo-front)。

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


## 主要功能

- **账号**：用户 / 商家 / 管理员三端登录、图形验证码、注册、JWT 刷新令牌。
- **商品**：商品与 SKU、分类与属性、上下架、按条件与关键词检索、热门商品。
- **交易**：购物车、下单、支付、订单状态流转，商家发货，管理端与商家端订单视图。
- **订单超时自动取消**：下单 15 分钟未支付自动置为「已取消」并归还库存。用 RabbitMQ 死信队列 + 队列级 TTL 实现延迟，`@Scheduled` 扫描兜底。
- **媒体**：MinIO 对象存储上传，轮播图与主页背景图管理。
- **接口日志**：AOP 采集 + traceId + MQ 批量入库，支持审计查询。


## License

[Apache License 2.0](LICENSE)
