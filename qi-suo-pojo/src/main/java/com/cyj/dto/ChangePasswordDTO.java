package com.cyj.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 修改密码（两次新密码确认由前端完成）
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String oldPassword;
    private String newPassword;
}
