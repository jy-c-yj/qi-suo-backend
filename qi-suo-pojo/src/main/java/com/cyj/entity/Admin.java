package com.cyj.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 管理员表实体类
 */
@Data
@TableName("admin")
public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 管理员ID
     */
    @TableId(value = "admin_id", type = IdType.AUTO)
    private Long adminId;

    /**
     * 用户名
     */
    @TableField("username")
    private String username;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 管理员类型：1-普通管理员，2-超级管理员
     */
    @TableField("admin_type")
    private Integer adminType;

    /**
     * 状态：0-禁用，1-启用
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField("create_by")
    private Long createBy;

    /**
     * 更新人
     */
    @TableField("update_by")
    private Long updateBy;

    /**
     * 是否删除：0-否，1-是
     */
    @TableField("is_deleted")
    @TableLogic
    private Integer isDeleted;

    // 可以添加一些常量定义，便于代码中使用
    public static class AdminType {
        public static final int NORMAL = 1; // 普通管理员
        public static final int SUPER = 2;  // 超级管理员
    }

    public static class Status {
        public static final int DISABLED = 0; // 禁用
        public static final int ENABLED = 1;  // 启用
    }

    public static class Deleted {
        public static final int NO = 0; // 未删除
        public static final int YES = 1; // 已删除
    }
}