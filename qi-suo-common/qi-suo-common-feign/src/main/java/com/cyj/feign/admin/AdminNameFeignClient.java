package com.cyj.feign.admin;


import com.cyj.back.AdminNameFeignClientFallBack;
import com.cyj.common.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 文件服务 -> 管理服务：按管理员id取用户名（图片审计字段的操作人）
 */
@FeignClient(value = "qi-suo-admin",
        contextId = "adminNameFeignClient",
        fallback = AdminNameFeignClientFallBack.class)
@Primary
public interface AdminNameFeignClient {

    /**
     * 按管理员id取用户名
     */
    @GetMapping("/api/admin/adminManagement/getAdminNameById")
    Result<String> getAdminNameById(@RequestParam("id") Long id);
}
