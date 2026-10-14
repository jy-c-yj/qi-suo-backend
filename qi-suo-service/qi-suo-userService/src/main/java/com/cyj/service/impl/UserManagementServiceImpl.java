package com.cyj.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dto.AdminUserListSelectConditionDTO;
import com.cyj.dvo.AdminUserManagementLDVO;
import com.cyj.entity.User;
import com.cyj.mapper.UserMapper;
import com.cyj.service.UserManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
//用户信息管理
public class UserManagementServiceImpl implements UserManagementService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 获取用户列表的分页结果
     * @param adminUserListSelectConditionDTO
     * @return 返回一个Result对象，其中包含分页数据PageResult<AdminUserManagementLDVO>
     */
    @Override
    public Result<PageResult<AdminUserManagementLDVO>> list(AdminUserListSelectConditionDTO adminUserListSelectConditionDTO) {

        //查询数据库
        Page<User> adminUserManagementLDVOPage = new Page<>(adminUserListSelectConditionDTO.getPageNum(), adminUserListSelectConditionDTO.getPageSize());
        List<AdminUserManagementLDVO> adminUserManagementLDVOList = userMapper.selectAdminUserList(adminUserManagementLDVOPage , adminUserListSelectConditionDTO);
        PageResult<AdminUserManagementLDVO> pageResult = new PageResult<>();
        if(adminUserManagementLDVOList!=null && !adminUserManagementLDVOList.isEmpty()){
            pageResult.setTotal(adminUserManagementLDVOPage.getTotal());
            pageResult.setRows(adminUserManagementLDVOList);
        }
        //返回结果
        return Result.success(pageResult);
    }

    /**
     * 用户账号状态切换
     * @param id 要切换状态的用户id
     * @param pageNum 分页页码
     * @return 返回操作结果
     */
    @Override
    public Result<String> setStatus(Long id, Integer pageNum) {
        //查询数据库获取当前状态
        User user = userMapper.selectById(id);
        if(user == null){
            return Result.error("该用户不存在");
        }
        if(user.getStatus() == 0){
            user.setStatus(1);
        }else{
            user.setStatus(0);
        }
        userMapper.updateById(user);
        return Result.success("修改成功");
    }
}
