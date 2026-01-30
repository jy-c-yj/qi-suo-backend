package com.cyj.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.constant.RedissonKey;
import com.cyj.dto.AdminDTO;
import com.cyj.dto.NewAdminDTO;
import com.cyj.dvo.AdminDVO;
import com.cyj.entity.Admin;
import com.cyj.mapper.AdminMapper;
import com.cyj.service.AdminManagementService;
import com.cyj.utils.RedissonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AdminManagementServiceImpl implements AdminManagementService {
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private RedissonUtil redissonUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 查询
     * @return
     */
    @Override
    public Result<PageResult<AdminDVO>> getAdminList(int pageNum, Long pageSize) {

        if(pageNum < 1 || pageSize < 1) {
            return Result.error("查询错误");
        }
        // 创建分页对象
        Page<Admin> page = new Page<>(pageNum, pageSize);
        //查找缓存
        String key = RedissonKey.ADMIN_KEY+"page:"+pageNum;
       List<AdminDVO> adminDVOList =  redissonUtil.getList(key);
        //存在直接返回
        if(adminDVOList != null && !adminDVOList.isEmpty()){
            PageResult<AdminDVO> pageResult = new PageResult<>();
            pageResult.setTotal(pageSize);
            pageResult.setRows(adminDVOList);
            return Result.success(pageResult);
        }

        //不存在查询数据库
        QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
        adminQueryWrapper.eq("is_deleted",false);
        adminQueryWrapper.orderByDesc("create_time");
        Page<Admin> adminPage = adminMapper.selectPage(page, adminQueryWrapper);
        List<Admin> adminList = adminPage.getRecords();
        if(adminList == null || adminList.isEmpty()){
            return Result.error("分页查询失败");
        }


        //添加缓存

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

        // 封装结果
        PageResult<AdminDVO> pageResult = new PageResult<>();
        pageResult.setTotal(adminPage.getTotal());
        pageResult.setRows(adminDVOList);


        //返回数据
        return Result.success(pageResult);
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
        if(newAdminDTO.getAdminName() == null || newAdminDTO.getAdminName().isEmpty()){
            log.error("数据不完整,缺少adminName");
            return Result.error("数据不完整");
        }
        if(newAdminDTO.getPhone() == null || newAdminDTO.getPhone().isEmpty()){
            log.error("数据不完整,缺少Phone");
            return Result.error("数据不完整");
        }
        if(newAdminDTO.getPassword() == null || newAdminDTO.getPassword().isEmpty()){
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
        newAdmin.setPassword(passwordEncoder.encode(newAdminDTO.getPassword()));
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
        //更新\删除缓存
        redissonUtil.deleteByPattern(RedissonKey.ADMIN_KEY+"page:*");
//        List<AdminDVO> adminDVOList = redissonUtil.getList(RedissonKey.ADMIN_KEY);
//
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
//
//        if(adminDVOList != null && !adminDVOList.isEmpty()){
//            adminDVOList.add(adminDVO);
//        }

        return Result.success(adminDVO);
    }

    /**
     * 设为启用
     * @param adminId
     * @return
     */
    @Override
    public Result<Boolean> setAdminIsEnabled(Long adminId,Integer pageNum) {
            return setAdminStatus(adminId,1,pageNum);
    }

    /**
     * 设为禁用
     * @param adminId
     * @return
     */
    @Override
    public Result<Boolean> setAdminNotEnabled(Long adminId,Integer pageNum) {
        return setAdminStatus(adminId,0,pageNum);
    }

    /**
     * 设置admin状态
     * @param adminId
     * @param status
     * @return
     */
    private Result<Boolean> setAdminStatus(Long adminId, Integer status,Integer pageNum) {
        //查询数据库
        Admin admin = adminMapper.selectById(adminId);
        if(admin == null){
            return  Result.error("未查询到此用户 -->" + adminId);
        }
        //更新状态

        admin.setStatus(status);
        admin.setUpdateTime(LocalDateTime.now());
        //查找更新人
        Admin admin1 = adminMapper.selectById(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        admin.setUpdateBy(admin1.getUsername());
        //判断权限
        if(admin1.getAdminType() <= admin.getAdminType()){
            return Result.error("权限不足");
        }
        //更新数据库
        adminMapper.updateById(admin);
        //更新缓存
        List<AdminDVO> adminDVOList = redissonUtil.getList(RedissonKey.ADMIN_KEY+"page:"+pageNum);
        for(int i = 0; i < adminDVOList.size(); i++){
            AdminDVO adminDVO = adminDVOList.get(i);
            if(adminDVO.getAdminId().equals(adminId)){
                adminDVOList.set(i,AdminDVO.builder()
                        .adminId(adminId)
                        .adminName(admin.getUsername())
                        .status(status)
                        .phone(admin.getPhone())
                        .adminType(admin.getAdminType())
                        .createBy(admin.getCreateBy())
                        .createTime(admin.getCreateTime())
                        .updateBy(admin.getUpdateBy())
                        .updateTime(admin.getUpdateTime())
                        .build());
            }
        }
        return Result.success(true);
    }

    /**
     * 修改admin信息
     * @param adminDTO
     * @return
     */
    @Override
    public Result<Boolean> updateAdmin(AdminDTO adminDTO,Integer pageNum) {

        Admin newAdmin = new Admin();


        if(adminDTO.getAdminType() == null && adminDTO.getAdminName() == null && adminDTO.getPhone() == null){
            return Result.error("数据不完整,无修改信息");
        }

        //判断数据是否完整
        if(adminDTO.getAdminId() == null){
            return Result.error("数据不完整,无adminId");
        }
        newAdmin.setAdminId(adminDTO.getAdminId());


        //获取修改者的权限
//        Collection<? extends GrantedAuthority> l = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
//        List<String> roles = l.stream().map(GrantedAuthority::getAuthority).toList();
//        int type = 0;
//        if(roles.contains(RoleConstants.OrdinaryADMIN)){
//            type = 1;
//        }else if (roles.contains(RoleConstants.SuperADMIN)){
//            type = 2;
//        } else if (roles.contains(RoleConstants.SystemADMIN)) {
//            type = 3;
//        }
        Admin admin = adminMapper.selectById(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        int type = admin.getAdminType();
        //查询被修改admin信息
        Admin updatedAdmin = adminMapper.selectById(adminDTO.getAdminId());

        //判断修改者的权限是否足够修改被修改者
        if(updatedAdmin.getAdminType() > type){
            return Result.error("权限不足");
        }

        //查询数据库是否重名
        if(adminDTO.getAdminName() != null){

            //查询数据库是否重名
            QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
            adminQueryWrapper.eq("username",adminDTO.getAdminName());
            Admin admin_ = adminMapper.selectOne(adminQueryWrapper);
            if(admin_ != null){
                return Result.error("该用户名重复");
            }
            newAdmin.setUsername(adminDTO.getAdminName());
        }

        //判断修改者的权限是否足够设置此等级
        if(adminDTO.getAdminType() != null){
            switch (adminDTO.getAdminType()){
                case 1:
                    if(type == 1){
                        return Result.error("权限不足");
                    }
                    break;
                    case 2:
                        if(type != 3){
                            return Result.error("权限不足");
                        }
                        break;
            }
            newAdmin.setAdminType(adminDTO.getAdminType());
        }

        //更新数据库信息
        if (adminDTO.getPhone() != null) {
            newAdmin.setPhone(adminDTO.getPhone());
        }

        newAdmin.setUpdateTime(LocalDateTime.now());
        newAdmin.setUpdateBy(admin.getUsername());

        adminMapper.updateById(newAdmin);
        //更新缓存
        List<AdminDVO> adminDVOList = redissonUtil.getList(RedissonKey.ADMIN_KEY+"page:"+pageNum);
        for (int i = 0; i < adminDVOList.size(); i++) {
            AdminDVO adminDVO = adminDVOList.get(i);
            if (adminDVO.getAdminId().equals(adminDTO.getAdminId())) {
                // 直接修改并设置回原位置
                if (adminDTO.getAdminName() != null) {
                    adminDVO.setAdminName(adminDTO.getAdminName());
                }
                if (adminDTO.getAdminType() != null) {
                    adminDVO.setAdminType(adminDTO.getAdminType());
                }
                if (adminDTO.getPhone() != null) {
                    adminDVO.setPhone(adminDTO.getPhone());
                }
                // 关键：同步到Redis
                adminDVOList.set(i, adminDVO);
                break;
            }
        }

        return Result.success(true);
    }

    /**
     * 删除admin
     * @param adminId
     * @return
     */
    @Override
    public Result<Boolean> deleteAdmin(Long adminId) {
        //查询被删除admin信息
        Admin admin = adminMapper.selectById(adminId);
        //查询删除者信息
        Admin admin_ = adminMapper.selectById(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        //判断权限
        if(admin_.getAdminType() != 3){
            return Result.error("权限不足，需要系统管理员");
        }
        //修改数据库
        Admin newadmin = new Admin();
        newadmin.setAdminId(adminId);
        newadmin.setIsDeleted(1);
        newadmin.setUpdateBy(admin_.getUsername());
        newadmin.setUpdateTime(LocalDateTime.now());
        adminMapper.updateById(newadmin);
        //更新缓存
        redissonUtil.deleteByPattern(RedissonKey.ADMIN_KEY+"page:*");
        return Result.success(true);
    }

}
