package com.cyj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyj.entity.SysOperLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
