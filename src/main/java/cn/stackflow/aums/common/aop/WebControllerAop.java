package cn.stackflow.aums.common.aop;

import cn.stackflow.aums.common.UserContextHolder;
import cn.stackflow.aums.common.utils.JSONUtils;
import cn.stackflow.aums.domain.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 使用AOP统一处理Web请求日志
 *
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-07-02 09:57
 */
@Aspect
@Component
@Slf4j
public class WebControllerAop {


    @Pointcut("execution(public * cn.stackflow.aums.web..*Controller.*(..)) && !@annotation(cn.stackflow.aums.common.aop.SkipWebLog)")
    public void webLog() {
    }

    /**
     * @param pjp
     * @return
     */
    @Around("webLog()")
    public Object arround(ProceedingJoinPoint pjp) throws Throwable {
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;

        if (log.isTraceEnabled()) {
            log.trace("WebControllerAop.arround");
        }
        User user = UserContextHolder.currentUser();
        if (user != null) {
            ThreadContext.put("user", user.getDept().getName() + "：" + user.getUsername() + " - " + user.getPhone());
        }
//            String ip = RequestUtils.getIp(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        String method = pjp.getSignature().getDeclaringType().getSimpleName() + "." + pjp.getSignature().getName();
        log.info("客户端请求参数：method: {}, req: {} {}", method, Arrays.toString(methodSignature.getParameterNames()), JSONUtils.object2Json(pjp.getArgs()));
        Object o = pjp.proceed();
        log.info("客户端返回参数：method: {}, res: {}", method, JSONUtils.object2Json(o));
        return o;
    }
}
