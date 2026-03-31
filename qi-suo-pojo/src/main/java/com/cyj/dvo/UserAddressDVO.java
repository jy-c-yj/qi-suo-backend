package com.cyj.dvo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

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
public class UserAddressDVO implements Serializable {

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
