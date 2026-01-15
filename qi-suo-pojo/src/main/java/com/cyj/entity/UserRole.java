package com.cyj.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

/**
 * 用户角色关联表实体类
 *
 * @author
 * @since
 */
@Data
@TableName("user_role")
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关联ID，主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 角色ID
     */
    @TableField("role_id")
    private Long roleId;

    /**
     * 是否生效
     */
    @TableField("is_active")
    private Boolean isActive;


    /**
     * 用户信息（非数据库字段）
     */
    @TableField(exist = false)
    private User user;

    /**
     * 角色信息（非数据库字段）
     */
    @TableField(exist = false)
    private Role role;

}
