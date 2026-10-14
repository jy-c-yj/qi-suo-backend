package com.cyj.controller;


import com.cyj.common.Result;
import com.cyj.dto.KaptchaDTO;
import com.cyj.dvo.KaptchaDVO;
import com.cyj.utils.RedissonUtil;
import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 验证码
 */

@RestController
@RequestMapping("/api/auth/kaptcha")
@Slf4j
public class KaptchaController {
    @Autowired
    private Producer producer;
    @Autowired
    private RedissonUtil redissonUtil;

    @GetMapping("/generate")
    public Result<KaptchaDVO> generate() {
        KaptchaDVO kaptchaDVO = new KaptchaDVO();
        try {
            //生成验证吗文本与图片
            String capText = producer.createText();
            BufferedImage image = producer.createImage(capText);

            //图片转base64编码
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", outputStream);
            byte[] bytes = outputStream.toByteArray();
            String base64Image = Base64.getEncoder().encodeToString(bytes);

            //将文本存入redis
            String key = "kaptcha:" + UUID.randomUUID();
            //设置过期时间
            redissonUtil.setValue(key, capText, 60, TimeUnit.SECONDS);
            kaptchaDVO.setBase64Image(base64Image);
            kaptchaDVO.setCaptchaID(key);
        } catch (Exception e) {
            log.error("验证码生成失败");

            return Result.error("验证码生成失败");
        }
        return Result.success(kaptchaDVO);
    }

    @PostMapping("/verify")
    public Result<String> verify(@RequestBody KaptchaDTO kaptchaDTO) {
        String key = kaptchaDTO.getCaptchaID();
        //获取用户输入的验证码，并转换为小写
        String inputCode = kaptchaDTO.getInputCode().toLowerCase();
        //从redis获取验证码文本
        String captchatxt = redissonUtil.getValue(key);
        //转换为小写
        captchatxt = captchatxt.toLowerCase();


        if (inputCode == null || inputCode.trim().isEmpty()) {
            return Result.error("验证码不能为空");
        } else if (captchatxt == null || captchatxt.trim().isEmpty()) {
            return Result.error("验证码已过期");
        } else if (!captchatxt.equals(inputCode)) {
            return Result.error("验证码输入错误");
        }

        return Result.success();

    }
}
