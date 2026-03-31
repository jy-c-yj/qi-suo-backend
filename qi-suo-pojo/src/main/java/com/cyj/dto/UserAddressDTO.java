package com.cyj.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户地址表
 * </p>
 *
 * @author cyj
 * @since 2026-03-11
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAddressDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;


    /**
     * 详细地址
     */
    private String detailAddress;

}
