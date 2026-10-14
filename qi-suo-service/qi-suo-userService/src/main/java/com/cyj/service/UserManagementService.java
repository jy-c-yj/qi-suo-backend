package com.cyj.service;

import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dto.AdminUserListSelectConditionDTO;
import com.cyj.dvo.AdminUserManagementLDVO;

/**
 * 用户信息管理：用户表由用户服务提供
 * （管理服务的用户管理页面经服务间调用走这里）
 */
public interface UserManagementService {

    /**
     * 分页查询用户列表
     */
    Result<PageResult<AdminUserManagementLDVO>> list(AdminUserListSelectConditionDTO adminUserListSelectConditionDTO);

    /**
     * 用户账号状态切换
     */
    Result<String> setStatus(Long id, Integer pageNum);
}
