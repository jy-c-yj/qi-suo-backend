package com.cyj.dvo;


import lombok.Data;

@Data
public class KaptchaDVO {

    /**
     * 生成的验证码的图片的base64编码
     */
    private String base64Image;
    /**
     * 验证码唯一标识
     */
    private String captchaID;
}
