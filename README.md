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

## 二、接口归属与"前缀例外"说明（重要）

接口经过一次归属调整，**对外契约（路径、HTTP 方法、参数、返回结构）完全没变**，前端无需改动。但调整后 **URL 前缀不再严格等于服务名**，排查问题时请以本表为准，不要按前缀推断提供方：

| 接口路径 | 由哪个服务提供 | 说明 |
|---|---|---|
| `/api/merchant/CategoryAndAttribute/**` | **管理服务** `qi-suo-admin` | 分类/属性的读接口，与 `/api/admin/category/**` 的写接口同属一个域，故归管理服务；URL 前缀保留 `/api/merchant` 以免前端改动 |
| `/api/user/register` | **认证服务** `qi-suo-auth` | 用户注册与登录/验证码同属账户入口闭环 |
| `/api/merchant/register` | **认证服务** `qi-suo-auth` | 同上 |
| `/api/admin/image/**` | **文件服务** `qi-suo-file` | 图片管理（轮播图、主页背景图）与对象存储上传同属媒体资源域 |

> `/api/merchant/**` 下的其余接口仍由商家服务提供，`/api/user/**` 下的其余接口仍由用户服务提供，`/api/admin/**` 下的其余接口仍由管理服务提供。

## 三、网关路由顺序

`qi-suo-gateway/src/main/resources/application.yml` 中的路由**自上而下先匹配先生效**，因此精确路径必须排在通配路径之前：

```yaml
- id: auth-route            # Path=/api/auth/**
- id: user-register-route   # Path=/api/user/register          ← 必须早于 user-route
- id: user-route            # Path=/api/user/**
- id: admin-image-route     # Path=/api/admin/image/**         ← 必须早于 admin-route
- id: admin-route           # Path=/api/admin/**
- id: merchant-category-attribute-route  # Path=/api/merchant/CategoryAndAttribute/**  ← 必须早于 merchant-route
- id: merchant-register-route           # Path=/api/merchant/register                ← 必须早于 merchant-route
- id: merchant-route        # Path=/api/merchant/**
- id: file-route            # Path=/api/file/**
```

调整路由顺序会导致接口被转发到错误的服务（表现为 404），改动前请确认这一点。

## 四、服务间调用（Feign）

业务代码中的跨服务调用只有一处：

- `AdminImageFeignClient`（`qi-suo-common-feign`）：**用户服务 → 文件服务**，调用 `GET /api/admin/image/getCarouselImageList`、`GET /api/admin/image/getNowHomeBackGroundImage`。

`FileFeign`（原有：管理服务 → 文件服务的上传调用）在图片管理并入文件服务后已无调用方，作为遗留代码保留在 `qi-suo-common-feign` 中。

## 五、构建与运行

Maven 未加入 PATH，请使用本机缓存的发行版；各服务打出来的是普通 thin jar（无 `Main-Class`，未做 repackage），**不能用 `java -jar` 启动**：

```bash
MVN=/c/Users/<你>/.m2/wrapper/dists/apache-maven-3.9.11-bin/*/apache-maven-3.9.11/bin/mvn.cmd

"$MVN" -DskipTests clean install
"$MVN" -pl qi-suo-service/qi-suo-adminService dependency:build-classpath -Dmdep.outputFile=cp.txt
java -cp "qi-suo-service/qi-suo-adminService/target/classes;$(cat cp.txt)" com.cyj.AdminServiceApplication
```

- 各服务通过 `application.yml` 的 `spring.profiles.include` 加载附加配置：`mybatis`（数据源 + MyBatis-Plus）、`minio`（文件服务）、`feign`（管理/用户服务）、`mq`（商家/用户服务）。
- 本地依赖的中间件：MySQL 3306、Redis 6379、MinIO 9000、Nacos 8848、RabbitMQ 5672。Nacos 未启动时可用 `--spring.cloud.nacos.discovery.enabled=false` 单独启动某个服务做自检（此时网关无法转发）。
- 数据库脚本未随仓库提供，需自备 `qi-suo` 库。
