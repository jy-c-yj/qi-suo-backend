package com.cyj.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.impl.NoNoise;
import com.google.code.kaptcha.impl.WaterRipple;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * Kaptcha验证码配置类（清晰易认版）
 * 核心优化：关闭扭曲、排除混淆字符、清晰字体
 */
@Configuration
public class KaptchaConfig {

    @Bean(name = "simpleKaptcha")
    public DefaultKaptcha getSimpleKaptcha() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();

        // 1. 基础尺寸配置
        properties.setProperty("kaptcha.image.width", "120");  // 宽度
        properties.setProperty("kaptcha.image.height", "45"); // 高度，适中更易认

        // 2. 字符配置（核心：排除易混淆字符）
        // 排除 0/O、1/I、l/L，保留 2-9、A-Z（无O、I）
        properties.setProperty("kaptcha.textproducer.char.string", "23456789ABCDEFGHJKLMNPQRSTUVWXYZ");
        properties.setProperty("kaptcha.textproducer.char.length", "4"); // 4位验证码（平衡安全与体验）
        properties.setProperty("kaptcha.textproducer.char.space", "5"); // 增大字符间距，避免重叠

        // 3. 字体配置（清晰无衬线字体）
        properties.setProperty("kaptcha.textproducer.font.names", "宋体,黑体,Microsoft YaHei");
        properties.setProperty("kaptcha.textproducer.font.size", "30"); // 字号适中
        properties.setProperty("kaptcha.textproducer.font.color", "33,33,33"); // 深灰色，高对比度

        // 4. 干扰项配置（弱化干扰，核心优化）
        properties.setProperty("kaptcha.noise.impl", NoNoise.class.getName()); // 轻微干扰（可注释关闭）
        properties.setProperty("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.ShadowGimpy"); // 为阴影样式，基本无扭曲

        // 5. 背景与边框配置
        properties.setProperty("kaptcha.background.clear.from", "248,248,248"); // 浅灰背景
        properties.setProperty("kaptcha.background.clear.to", "255,255,255"); // 白色背景（无渐变）
        properties.setProperty("kaptcha.border", "no"); // 去掉边框，视觉更简洁

        // 加载配置
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
