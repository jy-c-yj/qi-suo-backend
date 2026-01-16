package com.cyj.dto;


import lombok.Data;

@Data
public class KaptchaDTO {

    /**
     * 用户输入的验证码
     */
    private String inputCode;
    /**
     * 验证码唯一标识
     */
    private String captchaID;
}
