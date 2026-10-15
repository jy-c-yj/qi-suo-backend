package com.cyj.Annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)   // 关键
@Target(ElementType.METHOD)
public @interface Log {
    String module();        // 业务模块，如 "商品"
    String desc();          // 操作描述，如 "新增商品"
 }
