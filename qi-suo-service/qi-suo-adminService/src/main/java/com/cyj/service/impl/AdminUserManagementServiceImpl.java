package com.cyj.service.impl;

import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dto.AdminUserListSelectConditionDTO;
import com.cyj.dvo.AdminUserManagementLDVO;
import com.cyj.feign.user.UserManagementFeignClient;
import com.cyj.service.AdminUserManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
//管理员用户管理：用户数据归用户服务所有，本服务只做转发
public class AdminUserManagementServiceImpl implements AdminUserManagementService {

    @Autowired
    private UserManagementFeignClient userManagementFeignClient;

    /**
     * 获取用户列表的分页结果
     * @param adminUserListSelectConditionDTO
     * @return 返回一个Result对象，其中包含分页数据PageResult<AdminUserManagementLDVO>
     */
    @Override
    public Result<PageResult<AdminUserManagementLDVO>> list(AdminUserListSelectConditionDTO adminUserListSelectConditionDTO) {
        return userManagementFeignClient.list(adminUserListSelectConditionDTO);
    }

    /**
     * 用户账号状态切换
     * @param id 要切换状态的用户id
     * @param pageNum 分页页码
     * @return 返回操作结果
     */
    @Override
    public Result<String> status(Long id,Integer pageNum) {
        return userManagementFeignClient.setStatus(id, pageNum);
    }
}
