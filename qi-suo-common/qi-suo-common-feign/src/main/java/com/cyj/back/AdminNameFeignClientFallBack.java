package com.cyj.back;


import com.cyj.common.Result;
import com.cyj.feign.admin.AdminNameFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AdminNameFeignClientFallBack implements AdminNameFeignClient {

    @Override
    public Result<String> getAdminNameById(Long id) {
        log.error("远程调用 -> qi-suo-admin  方法 -> getAdminNameById 出错");
        return Result.error("获取管理员名失败");
    }
}
