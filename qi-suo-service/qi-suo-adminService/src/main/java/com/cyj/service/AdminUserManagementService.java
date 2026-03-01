package com.cyj.service;

import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dto.AdminUserListSelectConditionDTO;
import com.cyj.dvo.AdminUserManagementLDVO;

/**
 * 管理员用户管理服务接口
 * 该接口提供管理员用户列表查询功能
 */
public interface AdminUserManagementService {
    /**
     * 分页查询管理员用户列表
     *
     * @param adminUserListSelectConditionDTO
     * @return 返回分页结果，包含管理员用户列表信息
     */
    Result<PageResult<AdminUserManagementLDVO>> list(AdminUserListSelectConditionDTO adminUserListSelectConditionDTO);

/**
 * 获取指定ID的状态信息
 *
 * @param id 要查询状态的唯一标识符
 * @param pageNum 分页页码，用于分页查询状态信息
 * @return 返回一个包含String类型的状态信息的Result对象
 */
    Result<String> status(Long id,Integer pageNum);
}
