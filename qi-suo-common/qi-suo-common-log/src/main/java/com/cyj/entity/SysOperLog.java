package com.cyj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 接口请求日志 实体类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_oper_log")
public class SysOperLog implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * MQ 消息 id，幂等去重用
     */
    @TableField(value = "message_id")
    private String messageId;

    /**
     * 全局链路 id，网关生成
     */
    @TableField(value = "trace_id")
    private String traceId;

    /**
     * 来源服务，如 qi-suo-user
     */
    @TableField(value = "service_name")
    private String serviceName;

    /**
     * @Log(module)
     */
    @TableField(value = "module")
    private String module;

    /**
     * @Log(desc)
     */
    @TableField(value = "operation")
    private String operation;

    /**
     * 请求路径
     */
    @TableField(value = "uri")
    private String uri;

    /**
     * GET/POST/PUT/DELETE
     */
    @TableField(value = "http_method")
    private String httpMethod;

    /**
     * 客户端 ip（XFF 首段）
     */
    @TableField(value = "client_ip")
    private String clientIp;

    /**
     * 用户 id，-1 表未登录/取不到
     */
    @TableField(value = "user_id")
    private Long userId;

    @TableField(value = "role_name")
    private String roleName;
    /**
     * 脱敏后的请求参数 json，截断 4096 字符
     */
    @TableField(value = "request_param")
    private String requestParam;

    /**
     * 耗时毫秒
     */
    @TableField(value = "cost_ms")
    private Integer costMs;

    /**
     * Result.code，1 成功 0 失败
     */
    @TableField(value = "biz_code")
    private Integer bizCode;

    /**
     * 1 成功 0 失败
     */
    @TableField(value = "success")
    private Boolean success;

    /**
     * 异常类全限定名
     */
    @TableField(value = "error_type")
    private String errorType;

    /**
     * 异常消息 / 业务失败信息
     */
    @TableField(value = "error_msg")
    private String errorMsg;

    /**
     * 请求发生时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;
}
