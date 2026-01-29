package com.cyj.dto;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 管理员表实体类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewAdminDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 管理员ID
     */
    private Long adminId;

    /**
     * 管理员名
     */
    private String adminName;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 管理员类型：1-普通管理员，2-超级管理员
     */
    private Integer adminType;

}