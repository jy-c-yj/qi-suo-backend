package com.cyj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyj.entity.SysOperLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 接口请求日志 Mapper
 * <p>
 * 放在 com.cyj.mapper 是刻意的：全项目没有一处 @MapperScan，Mapper 靠 MyBatis-Plus
 * 自动配置扫主类所在包（各服务主类都在 com.cyj），所以引了本模块的服务零配置就能扫到。
 * <p>
 * 不用 XML：各服务配置的是 mybatis-plus.mapper-locations: classpath:mapper/*.xml，
 * 而现存 XML 实际在 resources/com/cyj/mapper/ 下（对不上），只是恰好都是空壳没暴露。
 * 注解 + &lt;script&gt; 绕开这个问题。
 */
@Mapper
public interface SysOperLogMapper extends BaseMapper<SysOperLog> {


    @Insert("""
            <script>
            INSERT INTO sys_oper_log (message_id, trace_id, service_name, module, operation,
                uri, http_method, client_ip, user_id, role_name, request_param,
                cost_ms, biz_code, success, error_type, error_msg, create_time)
            VALUES
            <foreach collection="logs" item="l" separator=",">
                (#{l.messageId}, #{l.traceId}, #{l.serviceName}, #{l.module}, #{l.operation},
                 #{l.uri}, #{l.httpMethod}, #{l.clientIp}, #{l.userId}, #{l.roleName}, #{l.requestParam},
                 #{l.costMs}, #{l.bizCode}, #{l.success}, #{l.errorType}, #{l.errorMsg}, #{l.createTime})
            </foreach>
            ON DUPLICATE KEY UPDATE message_id = message_id
            </script>
            """)
    int batchInsert(@Param("logs") List<SysOperLog> logs);
}
