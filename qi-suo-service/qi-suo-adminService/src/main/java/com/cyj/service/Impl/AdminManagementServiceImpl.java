package com.cyj.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cyj.common.Result;
import com.cyj.constant.RedissonKey;
import com.cyj.dto.NewAdminDTO;
import com.cyj.dvo.AdminDVO;
import com.cyj.entity.Admin;
import com.cyj.mapper.AdminMapper;
import com.cyj.service.AdminManagementService;
import com.cyj.utils.RedissonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class AdminManagementServiceImpl implements AdminManagementService {
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private RedissonUtil redissonUtil;

    /**
     * 查询
     * @return
     */
    @Override
    public Result<List<AdminDVO>> getAdminList() {
        //查找缓存
       List<AdminDVO> adminDVOList =  redissonUtil.getList(RedissonKey.ADMIN_KEY);
        //存在直接返回
        if(adminDVOList != null && !adminDVOList.isEmpty()){
            return Result.success(adminDVOList);
        }

        //不存在查询数据库
        QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
        adminQueryWrapper.eq("isDeleted",false);
        List<Admin> adminList = adminMapper.selectList(adminQueryWrapper);

        //添加缓存
        if (adminList != null && !adminList.isEmpty()) {
            adminList.forEach(admin -> {
                if (adminDVOList != null) {
                    adminDVOList.add(AdminDVO.builder()
                            .adminId(admin.getAdminId())
                            .adminName(admin.getUsername())
                            .adminType(admin.getAdminType())
                            .phone(admin.getPhone())
                            .createBy(admin.getCreateBy())
                            .createTime(admin.getCreateTime())
                            .status(admin.getStatus())
                            .updateBy(admin.getUpdateBy())
                            .updateTime(admin.getUpdateTime())
                            .build()
                    );
                }
            });
        } else {
            return Result.error("未查询到数据");
        }

        //返回数据
        return Result.success(adminDVOList);
    }

    /**
     * 新增
     * @param newAdminDTO
     * @return
     */
    @Override
    public Result<AdminDVO> newAdmin(NewAdminDTO newAdminDTO) {
        //判断数据完整性
        if(newAdminDTO == null){
            log.error("数据不完整 ---> newAdminDTO 为 null");
            return Result.error("数据不完整");
        }
        if(newAdminDTO.getAdminName() == null){
            log.error("数据不完整,缺少adminName");
            return Result.error("数据不完整");
        }
        if(newAdminDTO.getPhone() == null){
            log.error("数据不完整,缺少Phone");
            return Result.error("数据不完整");
        }
        if(newAdminDTO.getPassword() == null){
            log.error("数据不完整,缺少Password");
            return Result.error("数据不完整");
        }
        //判断管理员名是否重复
        QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
        adminQueryWrapper.eq("username",newAdminDTO.getAdminName());
        Admin admin = adminMapper.selectOne(adminQueryWrapper);

        //重复返回错误
        if(admin != null){
            return Result.error("该管理员已存在");
        }

        //不重复插入数据库
        Admin newAdmin = new Admin();
        newAdmin.setUsername(newAdminDTO.getAdminName());
        newAdmin.setPassword(newAdminDTO.getPassword());
        newAdmin.setStatus(1);
        newAdmin.setAdminType(newAdminDTO.getAdminType());
        newAdmin.setPhone(newAdminDTO.getPhone());
        newAdmin.setIsDeleted(0);

        newAdmin.setCreateTime(LocalDateTime.now());

        newAdmin.setUpdateTime(LocalDateTime.now());
        //查询创建者
        Admin cAdmin = adminMapper.selectById(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());

        newAdmin.setCreateBy(cAdmin.getUsername());
        newAdmin.setUpdateBy(cAdmin.getUsername());
        adminMapper.insert(newAdmin);
        //更新缓存
        List<AdminDVO> adminDVOList = redissonUtil.getList(RedissonKey.ADMIN_KEY);

        AdminDVO adminDVO = AdminDVO.builder()
                .adminId(newAdmin.getAdminId())
                .adminName(newAdmin.getUsername())
                .adminType(newAdmin.getAdminType())
                .phone(newAdmin.getPhone())
                .createBy(newAdmin.getCreateBy())
                .createTime(newAdmin.getCreateTime())
                .status(newAdmin.getStatus())
                .updateBy(newAdmin.getUpdateBy())
                .updateTime(newAdmin.getUpdateTime())
                .build();

        if(adminDVOList != null && !adminDVOList.isEmpty()){
            adminDVOList.add(adminDVO);
        }

        return Result.success(adminDVO);
    }
}
