package com.cyj.feign.user;


import com.cyj.back.UserManagementFeignClientFallBack;
import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dto.AdminUserListSelectConditionDTO;
import com.cyj.dvo.AdminUserManagementLDVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 管理服务 -> 用户服务：用户信息由用户服务提供
 */
@FeignClient(value = "qi-suo-user",
        contextId = "userManagementFeignClient",
        fallback = UserManagementFeignClientFallBack.class)
@Primary
public interface UserManagementFeignClient {

    /**
     * 用户列表查询
     */
    @PostMapping("/api/user/userManagement/list")
    Result<PageResult<AdminUserManagementLDVO>> list(@RequestBody AdminUserListSelectConditionDTO adminUserListSelectConditionDTO);

    /**
     * 用户账号状态切换
     */
    @PutMapping("/api/user/userManagement/status")
    Result<String> setStatus(@RequestParam("id") Long id, @RequestParam("pageNum") Integer pageNum);
}
