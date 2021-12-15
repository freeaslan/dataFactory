package com.esign.service.dfplatform.aop;

import java.lang.annotation.*;

/**
 * @Author: huangtai
 * @Description: 定义操作日志注解
 * @Date: 2021/8/3 16:41
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperateLogger {

    String operate() default "";
}
