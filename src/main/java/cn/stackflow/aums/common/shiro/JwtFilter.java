package cn.stackflow.aums.common.shiro;

import cn.stackflow.aums.common.Result;
import cn.stackflow.aums.common.ResultCode;
import cn.stackflow.aums.common.utils.JSONUtils;
import cn.stackflow.aums.common.utils.SpringUtils;
import cn.stackflow.aums.common.utils.StringUtils;
import cn.stackflow.aums.domain.entity.User;
import cn.stackflow.aums.domain.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义一个Filter，用来拦截所有的请求判断是否携带Token
 * isAccessAllowed()判断是否携带了有效的JwtToken
 * onAccessDenied()是没有携带JwtToken的时候进行账号密码登录，登录成功允许访问，登录失败拒绝访问
 *
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-07-03 11:11
 */
@Slf4j
public class JwtFilter extends AccessControlFilter {

    private static ThreadLocal<Result> resultThreadLocal = new ThreadLocal<Result>();


    JwtUtil jwtUtil = new JwtUtil();

    public final static String HEADER_AUTHORIZATION = "Authorization";
    public final static String CONTENT_TYPE_APPLICATION_JSON = "application/json;charset=utf-8";

    /*
     * 1. 返回true，shiro就直接允许访问url
     * 2. 返回false，shiro才会根据onAccessDenied的方法的返回值决定是否允许访问url
     * */
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object mappedValue) throws Exception {
        if (log.isTraceEnabled()) {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            log.trace("isAccessAllowed URI：{}", request.getRequestURI());
            log.trace("isAccessAllowed 方法被调用");
        }
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = request.getHeader(HEADER_AUTHORIZATION);

        if (StringUtils.isEmpty(jwt)) {
            resultThreadLocal.set(new Result()
                    .setCode(ResultCode.UNAUTHORIZED)
                    .setMessage("登录已过期"));
            return false;
        }
        jwt = jwt.replace("Bearer ","");
        if (!jwtUtil.isVerify(jwt)) {// 带了token并且token可用 直接放行
            log.error("token不可用");
            resultThreadLocal.set(new Result()
                    .setCode(ResultCode.UNAUTHORIZED)
                    .setMessage("登录已过期"));
            return false;
        }
        Claims decode = jwtUtil.decode(jwt);
        UserService userService = SpringUtils.getBean(UserService.class);
        User user = userService.get(decode.get("id", String.class));
        if (user == null) {
            resultThreadLocal.set(new Result()
                    .setCode(ResultCode.UNAUTHORIZED)
                    .setMessage("登录已过期"));
            return false;
        }
        UsernamePasswordJWTToken usernamePasswordToken = new UsernamePasswordJWTToken(user.getUsername(), user.getPassword(), true);
        getSubject(servletRequest, servletResponse).login(usernamePasswordToken);
        Subject subject = SecurityUtils.getSubject();
        if (log.isTraceEnabled()) {
            log.trace("isAuthenticated:" + subject.isAuthenticated());
            log.trace(HEADER_AUTHORIZATION + ":{}", jwt);
            log.trace("RequestURI:{}", request.getRequestURI());
        }
        //判断动态权限


        return true;
    }

    /**
     * 返回结果为true表明登录通过
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        if (log.isTraceEnabled()) {
            log.trace("onAccessDenied 方法被调用");
        }
        onLoginFail(servletResponse);
        return false;
    }

    //登录失败时默认返回 401 状态码
    private void onLoginFail(ServletResponse response) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        Result result = resultThreadLocal.get();
        httpResponse.setStatus(result.getCode());
//        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.setContentType(CONTENT_TYPE_APPLICATION_JSON);
        httpResponse.getWriter().write(JSONUtils.object2Json(result));
    }
}
