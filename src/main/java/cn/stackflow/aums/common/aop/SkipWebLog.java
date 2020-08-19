package cn.stackflow.aums.common.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 跳过 WebControllerAop 的请求日志打印
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-07-18 11:54
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface SkipWebLog {
}
