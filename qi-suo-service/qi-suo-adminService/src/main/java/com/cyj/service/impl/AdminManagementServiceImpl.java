package com.cyj.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.constants.RoleConstants;
import com.cyj.dto.AdminDTO;
import com.cyj.dto.NewAdminDTO;
import com.cyj.dvo.AdminDVO;
import com.cyj.entity.Admin;
import com.cyj.mapper.AdminMapper;
import com.cyj.service.AdminManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class AdminManagementServiceImpl implements AdminManagementService {
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 当前操作者信息
     */
    private static final class OperatorInfo {
        private final String name;
        private final int type;
        private final Long adminId;

        private OperatorInfo(String name, int type, Long adminId) {
            this.name = name;
            this.type = type;
            this.adminId = adminId;
        }
    }

    /**
     * 取当前操作者：
     * - 服务间调用（Feign 注入 system，principal 为 -1）按最高级别处理，不再回查管理员表
     * - 管理员调用按 principal id 查 admin 表，以 admin_type 为准
     */
    private OperatorInfo currentOperator() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        boolean system = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(RoleConstants.SYSTEM::equals);
        if (system) {
            return new OperatorInfo(RoleConstants.SYSTEM, 3, -1L);
        }
        if (!(authentication.getPrincipal() instanceof Number principal)) {
            return null;
        }
        Admin operator = adminMapper.selectById(principal.longValue());
        if (operator == null || operator.getAdminType() == null) {
            return null;
        }
        return new OperatorInfo(operator.getUsername(), operator.getAdminType(),
                operator.getAdminId() == null ? principal.longValue() : operator.getAdminId());
    }

    /**
     * 目标是否可被当前操作者管理：系统管理员可管理所有；其他人只能管理平级与下一级
     */
    private boolean canManage(OperatorInfo operator, Admin target) {
        if (target == null || target.getAdminType() == null) {
            return false;
        }
        return operator.type == 3 || target.getAdminType() <= operator.type;
    }

    /**
     * 查询：直接查库，禁用（status=0）的管理员也要显示
     */
    @Override
    public Result<PageResult<AdminDVO>> getAdminList(int pageNum, Long pageSize) {

        if (pageNum < 1 || pageSize == null || pageSize < 1) {
            return Result.error("查询错误");
        }
        Page<Admin> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
        adminQueryWrapper.eq("is_deleted", false);
        adminQueryWrapper.orderByDesc("create_time");
        Page<Admin> adminPage = adminMapper.selectPage(page, adminQueryWrapper);

        List<AdminDVO> rows = adminPage.getRecords().stream()
                .map(admin -> AdminDVO.builder()
                        .adminId(admin.getAdminId())
                        .adminName(admin.getUsername())
                        .adminType(admin.getAdminType())
                        .phone(admin.getPhone())
                        .createBy(admin.getCreateBy())
                        .createTime(admin.getCreateTime())
                        .status(admin.getStatus())
                        .updateBy(admin.getUpdateBy())
                        .updateTime(admin.getUpdateTime())
                        .build())
                .toList();

        PageResult<AdminDVO> pageResult = new PageResult<>();
        pageResult.setTotal(adminPage.getTotal());
        pageResult.setRows(rows);
        return Result.success(pageResult);
    }

    /**
     * 新增：普通管理员不能新增；新管理员级别不高于自己，且不能创建系统管理员（除非自己就是系统管理员）
     */
    @Override
    public Result<AdminDVO> newAdmin(NewAdminDTO newAdminDTO) {
        if (newAdminDTO == null) {
            log.error("数据不完整 ---> newAdminDTO 为 null");
            return Result.error("数据不完整");
        }
        if (newAdminDTO.getAdminName() == null || newAdminDTO.getAdminName().isEmpty()) {
            log.error("数据不完整,缺少adminName");
            return Result.error("数据不完整");
        }
        if (newAdminDTO.getPhone() == null || newAdminDTO.getPhone().isEmpty()) {
            log.error("数据不完整,缺少Phone");
            return Result.error("数据不完整");
        }
        if (newAdminDTO.getPassword() == null || newAdminDTO.getPassword().isEmpty()) {
            log.error("数据不完整,缺少Password");
            return Result.error("数据不完整");
        }
        if (newAdminDTO.getAdminType() == null
                || newAdminDTO.getAdminType() < 1 || newAdminDTO.getAdminType() > 3) {
            return Result.error("管理员级别不合法");
        }

        OperatorInfo operator = currentOperator();
        if (operator == null) {
            return Result.error("获取当前管理员失败");
        }
        if (operator.type < 2) {
            return Result.error("权限不足");
        }
        if (newAdminDTO.getAdminType() > operator.type) {
            return Result.error("权限不足");
        }
        if (newAdminDTO.getAdminType() == 3 && operator.type != 3) {
            return Result.error("权限不足");
        }

        QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
        adminQueryWrapper.eq("username", newAdminDTO.getAdminName());
        Admin admin = adminMapper.selectOne(adminQueryWrapper);
        if (admin != null) {
            return Result.error("该管理员已存在");
        }

        Admin newAdmin = new Admin();
        newAdmin.setUsername(newAdminDTO.getAdminName());
        newAdmin.setPassword(passwordEncoder.encode(newAdminDTO.getPassword()));
        newAdmin.setStatus(1);
        newAdmin.setAdminType(newAdminDTO.getAdminType());
        newAdmin.setPhone(newAdminDTO.getPhone());
        newAdmin.setIsDeleted(0);
        newAdmin.setCreateTime(LocalDateTime.now());
        newAdmin.setUpdateTime(LocalDateTime.now());
        newAdmin.setCreateBy(operator.name);
        newAdmin.setUpdateBy(operator.name);
        adminMapper.insert(newAdmin);

        return Result.success(AdminDVO.builder()
                .adminId(newAdmin.getAdminId())
                .adminName(newAdmin.getUsername())
                .adminType(newAdmin.getAdminType())
                .phone(newAdmin.getPhone())
                .createBy(newAdmin.getCreateBy())
                .createTime(newAdmin.getCreateTime())
                .status(newAdmin.getStatus())
                .updateBy(newAdmin.getUpdateBy())
                .updateTime(newAdmin.getUpdateTime())
                .build());
    }

    @Override
    public Result<Boolean> setAdminIsEnabled(Long adminId, Integer pageNum) {
        return setAdminStatus(adminId, 1);
    }

    @Override
    public Result<Boolean> setAdminNotEnabled(Long adminId, Integer pageNum) {
        return setAdminStatus(adminId, 0);
    }

    private Result<Boolean> setAdminStatus(Long adminId, Integer status) {
        OperatorInfo operator = currentOperator();
        if (operator == null) {
            return Result.error("获取当前管理员失败");
        }
        Admin admin = adminMapper.selectById(adminId);
        if (admin == null) {
            return Result.error("未查询到此用户 -->" + adminId);
        }
        // 不能启停自己
        if (Objects.equals(admin.getAdminId(), operator.adminId)) {
            return Result.error("不能修改自己的状态");
        }
        if (!canManage(operator, admin)) {
            return Result.error("权限不足");
        }
        // 系统管理员只能由系统管理员操作（非系统操作者已在 canManage 中拦下）
        if (admin.getAdminType() != null && admin.getAdminType() == 3 && operator.type != 3) {
            return Result.error("权限不足");
        }

        Admin update = new Admin();
        update.setAdminId(adminId);
        update.setStatus(status);
        update.setUpdateTime(LocalDateTime.now());
        update.setUpdateBy(operator.name);
        adminMapper.updateById(update);
        return Result.success(true);
    }

    /**
     * 修改：目标必须可管理；不能修改自己的级别；新级别不高于自己；不能设置成系统管理员（除非自己就是）
     */
    @Override
    public Result<Boolean> updateAdmin(AdminDTO adminDTO, Integer pageNum) {

        if (adminDTO == null) {
            return Result.error("数据不完整");
        }
        if (adminDTO.getAdminType() == null && adminDTO.getAdminName() == null && adminDTO.getPhone() == null) {
            return Result.error("数据不完整,无修改信息");
        }
        if (adminDTO.getAdminId() == null) {
            return Result.error("数据不完整,无adminId");
        }

        OperatorInfo operator = currentOperator();
        if (operator == null) {
            return Result.error("获取当前管理员失败");
        }
        Admin target = adminMapper.selectById(adminDTO.getAdminId());
        if (target == null) {
            return Result.error("管理员不存在");
        }
        if (!canManage(operator, target)) {
            return Result.error("权限不足");
        }
        if (target.getAdminType() != null && target.getAdminType() == 3 && operator.type != 3) {
            return Result.error("权限不足");
        }

        Admin update = new Admin();
        update.setAdminId(adminDTO.getAdminId());
        update.setUpdateTime(LocalDateTime.now());
        update.setUpdateBy(operator.name);

        if (adminDTO.getAdminType() != null) {
            // 不能修改自己的级别
            if (Objects.equals(target.getAdminId(), operator.adminId)) {
                return Result.error("不能修改自己的级别");
            }
            if (adminDTO.getAdminType() < 1 || adminDTO.getAdminType() > 3) {
                return Result.error("管理员级别不合法");
            }
            if (adminDTO.getAdminType() > operator.type) {
                return Result.error("权限不足");
            }
            if (adminDTO.getAdminType() == 3 && operator.type != 3) {
                return Result.error("权限不足");
            }
            update.setAdminType(adminDTO.getAdminType());
        }

        if (adminDTO.getPhone() != null) {
            update.setPhone(adminDTO.getPhone());
        }

        adminMapper.updateById(update);
        return Result.success(true);
    }

    /**
     * 删除：仍为逻辑删除，权限按新增同级收紧（系统/超级管理员）
     */
    @Override
    public Result<Boolean> deleteAdmin(Long adminId) {
        OperatorInfo operator = currentOperator();
        if (operator == null) {
            return Result.error("获取当前管理员失败");
        }
        if (operator.type < 2) {
            return Result.error("权限不足，需要超级管理员或系统管理员");
        }
        Admin admin = adminMapper.selectById(adminId);
        if (admin == null) {
            return Result.error("管理员不存在");
        }
        if (Objects.equals(admin.getAdminId(), operator.adminId)) {
            return Result.error("不能删除自己");
        }
        if (!canManage(operator, admin)) {
            return Result.error("权限不足");
        }
        if (admin.getAdminType() != null && admin.getAdminType() == 3 && operator.type != 3) {
            return Result.error("权限不足");
        }

        Admin newadmin = new Admin();
        newadmin.setAdminId(adminId);
        newadmin.setIsDeleted(1);
        newadmin.setUpdateBy(operator.name);
        newadmin.setUpdateTime(LocalDateTime.now());
        adminMapper.updateById(newadmin);
        return Result.success(true);
    }

    /**
     * 按管理员id取用户名（其他服务需要操作人/审计名时使用）
     */
    @Override
    public Result<String> getAdminNameById(Long id) {
        if (id == null) {
            return Result.error("管理员id为空");
        }
        Admin admin = adminMapper.selectById(id);
        if (admin == null) {
            return Result.error("管理员不存在");
        }
        //管理员名要放进 data：Result.success(String) 重载会把值塞进 msg，所以这里手工塞 data
        Result<String> result = Result.success();
        result.setData(admin.getUsername());
        return result;
    }

}
