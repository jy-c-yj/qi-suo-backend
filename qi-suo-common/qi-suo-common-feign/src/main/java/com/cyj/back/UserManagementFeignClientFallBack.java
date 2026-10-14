package com.cyj.back;


import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dto.AdminUserListSelectConditionDTO;
import com.cyj.dvo.AdminUserManagementLDVO;
import com.cyj.feign.user.UserManagementFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserManagementFeignClientFallBack implements UserManagementFeignClient {

    @Override
    public Result<PageResult<AdminUserManagementLDVO>> list(AdminUserListSelectConditionDTO adminUserListSelectConditionDTO) {
        log.error("远程调用 -> qi-suo-user  方法 -> list 出错");
        return Result.error("获取用户列表失败");
    }

    @Override
    public Result<String> setStatus(Long id, Integer pageNum) {
        log.error("远程调用 -> qi-suo-user  方法 -> setStatus 出错");
        return Result.error("修改用户状态失败");
    }
}
