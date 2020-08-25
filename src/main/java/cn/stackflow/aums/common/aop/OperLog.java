package cn.stackflow.aums.common.aop;


import java.lang.annotation.*;

/**
 * 自定义操作日志注解
 *
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-08-25 10:30
 */
@Target(ElementType.METHOD) //注解放置的目标位置,METHOD是可注解在方法级别上
@Retention(RetentionPolicy.RUNTIME) //注解在哪个阶段执行
@Documented
public @interface OperLog {
    String operModul() default ""; // 操作模块

    String operType() default "";  // 操作类型

    String operDesc() default "";  // 操作说明
}
