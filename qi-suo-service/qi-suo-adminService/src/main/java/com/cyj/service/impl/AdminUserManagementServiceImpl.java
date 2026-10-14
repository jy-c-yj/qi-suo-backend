package com.cyj.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.constant.RedissonKey;
import com.cyj.dto.AdminUserListSelectConditionDTO;
import com.cyj.dvo.AdminUserManagementLDVO;
import com.cyj.entity.User;
import com.cyj.mapper.UserMapper;
import com.cyj.service.AdminUserManagementService;
import com.cyj.utils.RedissonUtil;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
//管理员用户管理
public class AdminUserManagementServiceImpl implements AdminUserManagementService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedissonUtil redissonUtil;



 /**
 * 获取管理员用户列表的分页结果
 * @param adminUserListSelectConditionDTO
 * @return 返回一个Result对象，其中包含分页数据PageResult<AdminUserManagementLDVO>
 *         AdminUserManagementLDVO是管理员用户列表的数据传输对象
 */
    @Override
    public Result<PageResult<AdminUserManagementLDVO>> list(AdminUserListSelectConditionDTO adminUserListSelectConditionDTO) {


        //查询缓存
        String key = RedissonKey.ADMIN_MANAGEMENT_USER_KEY+ adminUserListSelectConditionDTO.getPageNum();
//        List<AdminUserManagementLDVO> list = redissonUtil.getList(key);
        //存在返回
//        if(list!=null && !list.isEmpty()){
//            PageResult<AdminUserManagementLDVO> pageResult = new PageResult<>();
//            pageResult.setRows(list);
//            pageResult.setTotal((long) list.size());
//            return Result.success(pageResult);
//        }
        //不存在查询数据库
        Page<User> adminUserManagementLDVOPage = new Page<>(adminUserListSelectConditionDTO.getPageNum(), adminUserListSelectConditionDTO.getPageSize());
        List<AdminUserManagementLDVO> adminUserManagementLDVOList = userMapper.selectAdminUserList(adminUserManagementLDVOPage , adminUserListSelectConditionDTO);
        PageResult<AdminUserManagementLDVO> pageResult = new PageResult<>();
        if(adminUserManagementLDVOList!=null && !adminUserManagementLDVOList.isEmpty()){
            pageResult.setTotal(adminUserManagementLDVOPage.getTotal());
            pageResult.setRows(adminUserManagementLDVOList);
            //存入缓存
//            redissonUtil.addAllToList(key,adminUserManagementLDVOList);
//            redissonUtil.expire(key,30, TimeUnit.MINUTES);
        }
        //返回结果
        return Result.success(pageResult);
    }

/**
 * 重写status方法，用于获取指定ID的状态信息
 * @param id 要查询状态的ID
 * @return 返回一个Result对象，其中包含状态信息字符串，当前实现返回null
 */
    @Override
    public Result<String> status(Long id,Integer pageNum) {
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
        //更新缓存
//        String key = RedissonKey.ADMIN_MANAGEMENT_USER_KEY+pageNum;
//        List<AdminUserManagementLDVO> list = redissonUtil.getList(key);
//        for (int i = 0; i < list.size(); i++) {
//            AdminUserManagementLDVO adminUserManagementLDVO = list.get(i);
//            if(adminUserManagementLDVO.getId().equals(id)){
//                adminUserManagementLDVO.setStatus(user.getStatus());
//                list.set(i,adminUserManagementLDVO);
//                break;
//            }
//        }
        return Result.success("修改成功");
    }
}
