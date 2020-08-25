package cn.stackflow.aums.common.aop;

import cn.stackflow.aums.common.UserContextHolder;
import cn.stackflow.aums.common.utils.JSONUtils;
import cn.stackflow.aums.common.utils.RequestUtils;
import cn.stackflow.aums.domain.entity.OperLogs;
import cn.stackflow.aums.domain.entity.User;
import cn.stackflow.aums.domain.service.OperLogsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.ThreadContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

    @Autowired
    OperLogsService operLogsService;


    @Pointcut("execution(public * cn.stackflow.aums.web..*Controller.*(..)) && !@annotation(cn.stackflow.aums.common.aop.SkipWebLog)")
    public void webLog() {
    }

    /**
     * @param pjp
     * @return
     */
    @Order(1)
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
        String method = pjp.getSignature().getDeclaringType().getSimpleName() + "." + pjp.getSignature().getName();
        log.info("客户端请求参数：method: {}, req: {} {}", method, Arrays.toString(methodSignature.getParameterNames()), JSONUtils.object2Json(pjp.getArgs()));
        Object o = pjp.proceed();
        log.info("客户端返回参数：method: {}, res: {}", method, JSONUtils.object2Json(o));
        return o;
    }


    /**
     * @param pjp
     * @return
     */
    @Order(2)
    @Around("@annotation(cn.stackflow.aums.common.aop.OperLog)")
    public Object operLogs(ProceedingJoinPoint pjp) throws Throwable {
        Object o = null;
        try {
            o = pjp.proceed();
            OperLogs operlog = createOperLogs(pjp, o);
            operlog.setLogsType(1);
            operLogsService.insert(operlog);
        } catch (Throwable e) {
            OperLogs operlog = createOperLogs(pjp, o);
            operlog.setLogsType(2);
            operlog.setThrowable(stackTraceToString(e.getClass().getName(), e.getMessage(), e.getStackTrace())); // 异常信息
            operLogsService.insert(operlog);
            throw e;
        }
        return o;
    }


    private OperLogs createOperLogs(ProceedingJoinPoint pjp, Object o) {
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);

        User user = UserContextHolder.currentUser();
        OperLogs operlog = new OperLogs();
        // 获取切入点所在的方法
        Method method = methodSignature.getMethod();
        // 获取操作
        OperLog opLog = method.getAnnotation(OperLog.class);
        if (opLog != null) {
            String operModul = opLog.operModul();
            String operType = opLog.operType();
            String operDesc = opLog.operDesc();
            operlog.setOperModule(operModul); // 操作模块
            operlog.setOperType(operType); // 操作类型
            operlog.setOperDesc(operDesc); // 操作描述
        }
        String className = pjp.getTarget().getClass().getName();
        operlog.setOperMethod(className + "." + method.getName()); // 请求方法
        String params = JSONUtils.object2Json(pjp.getArgs());
        operlog.setOperReqParam(params); // 请求参数
        operlog.setOperRespParam(JSONUtils.object2Json(o)); // 返回结果
        if(user != null){
            operlog.setOperUserId(user.getId()); // 请求用户ID
            operlog.setOperUserName(user.getUsername()); // 请求用户名称
        }
        operlog.setOperIp(RequestUtils.getRemoteAddr(request)); // 请求IP
        operlog.setOperUri(request.getRequestURI()); // 请求URI
        operlog.setCreateTime(LocalDateTime.now()); // 创建时间

        return operlog;
    }

    /**
     * 转换request 请求参数
     *
     * @param paramMap request获取的参数数组
     */
    public Map<String, String> converMap(Map<String, String[]> paramMap) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        for (String key : paramMap.keySet()) {
            rtnMap.put(key, paramMap.get(key)[0]);
        }
        return rtnMap;
    }

    /**
     * 转换异常信息为字符串
     *
     * @param exceptionName    异常名称
     * @param exceptionMessage 异常信息
     * @param elements         堆栈信息
     */
    public String stackTraceToString(String exceptionName, String exceptionMessage, StackTraceElement[] elements) {
        StringBuffer strbuff = new StringBuffer();
        for (StackTraceElement stet : elements) {
            strbuff.append(stet + "\n");
        }
        String message = exceptionName + ":" + exceptionMessage + "\n\t" + strbuff.toString();
        return message;
    }
}
