-- 接口请求日志表
-- 归属库：qi-suo（各服务共用；admin 的 application-mybatis.yml 指向它）
-- 消费端：qi-suo-sys-log 的 RequestLogConsumer，攒批 ON DUPLICATE KEY UPDATE 写入
-- 执行：mysql -uroot -p1234 qi-suo < docs/sql/sys_oper_log.sql
--
-- 字段与 com.cyj.entity.SysOperLog 严格对齐：
--   * 没有 http_status —— 实体和切面都未采集（成败口径是 biz_code / success）
--   * 有 role_name —— 实体 SysOperLog.roleName，切面 currentRoles() 填充

CREATE TABLE IF NOT EXISTS `sys_oper_log` (
  `id`            BIGINT UNSIGNED   NOT NULL AUTO_INCREMENT           COMMENT '主键',
  `message_id`    CHAR(36)          NOT NULL                          COMMENT 'MQ 消息 id（带横线 UUID），幂等去重用',
  `trace_id`      CHAR(32)          NOT NULL DEFAULT ''               COMMENT '全局链路 id（无横线 UUID），网关生成',
  `service_name`  VARCHAR(32)       NOT NULL DEFAULT ''               COMMENT '来源服务，如 qi-suo-admin',
  `module`        VARCHAR(64)       NOT NULL DEFAULT ''               COMMENT '@Log(module)',
  `operation`     VARCHAR(128)      NOT NULL DEFAULT ''               COMMENT '@Log(desc)',
  `uri`           VARCHAR(255)      NOT NULL DEFAULT ''               COMMENT '请求路径',
  `http_method`   VARCHAR(10)       NOT NULL DEFAULT ''               COMMENT 'GET/POST/PUT/DELETE',
  `client_ip`     VARCHAR(64)       NOT NULL DEFAULT ''               COMMENT '客户端 ip（XFF 首段）',
  `user_id`       BIGINT            NOT NULL DEFAULT -1               COMMENT '用户 id，-1 表未登录/取不到',
  `role_name`     VARCHAR(255)      NOT NULL DEFAULT ''               COMMENT '角色，逗号分隔',
  `request_param` TEXT              NULL                              COMMENT '脱敏后的请求参数 json，应用层截断 4096 字符',
  `cost_ms`       INT UNSIGNED      NOT NULL DEFAULT 0                COMMENT '耗时毫秒',
  `biz_code`      INT               NOT NULL DEFAULT 0                COMMENT 'Result.code，1 成功 0 失败',
  `success`       TINYINT(1)        NOT NULL DEFAULT 1                COMMENT '1 成功 0 失败',
  `error_type`    VARCHAR(255)      NOT NULL DEFAULT ''               COMMENT '异常类全限定名',
  `error_msg`     VARCHAR(1024)     NOT NULL DEFAULT ''               COMMENT '异常消息 / 业务失败信息',
  `create_time`   DATETIME(3)       NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '请求发生时间，由应用显式写入',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_message_id` (`message_id`),
  KEY `idx_trace_id`    (`trace_id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_user_time`   (`user_id`, `create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='接口请求日志';


-- ─────────────────────────────────────────────────────────────────────────────
-- 既有表对齐（表是此前手工建的，2026-10-16 核对出下面这一处不一致，需跑一次）
--
-- 现存表里 role_name 是 VARCHAR(10)，而 role_name 存的是
-- SecurityContext 里 authorities 的逗号拼接：单个 ROLE_ADMIN 恰好 10 字符，
-- 平时看不出问题；用户一旦有两个角色（如 ROLE_ADMIN,ROLE_USER = 20 字符），
-- 落库会抛 ERROR 1406 Data too long，该条日志被消费者丢进死信队列。
-- 实测：
--   INSERT ... role_name='ROLE_ADMIN,ROLE_USER' → ERROR 1406 (22001)
-- ─────────────────────────────────────────────────────────────────────────────
ALTER TABLE `sys_oper_log`
  MODIFY COLUMN `role_name` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '角色，逗号分隔';

