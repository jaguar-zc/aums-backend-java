package cn.stackflow.aums.web;

import cn.stackflow.aums.common.Result;
import cn.stackflow.aums.common.aop.OperLog;
import cn.stackflow.aums.common.bean.LoginReq;
import cn.stackflow.aums.common.bean.LoginResp;
import cn.stackflow.aums.common.constant.Constants;
import cn.stackflow.aums.common.shiro.JwtUtil;
import cn.stackflow.aums.common.shiro.UsernamePasswordJWTToken;
import cn.stackflow.aums.domain.entity.User;
import cn.stackflow.aums.common.ResultBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-07-02 10:19
 */
@Api("登录")
@Slf4j
@RestController
@RequestMapping(ApiVersion.VERSION)
public class LoginController {

    JwtUtil jwtUtil = new JwtUtil();

    @Value("${spring.token-expire:86400}")
    Long tokenExpire;

    @OperLog(operModul = "用户",operType = Constants.OPER_TYPE_LOGIN,operDesc = Constants.OPER_TYPE_LOGIN)
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<LoginResp> login(@RequestBody @Valid LoginReq loginReq) {
        String error = "";
        try {
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordJWTToken usernamePasswordToken = new UsernamePasswordJWTToken(loginReq.getUsername(), loginReq.getPassword(), false);
            //进行验证，这里可以捕获异常，然后返回对应信息
            subject.login(usernamePasswordToken);
            return ResultBuilder.success(new LoginResp(getToken()));
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            error = "用户名/密码错误";
            log.error("用户名不存在");
        } catch (IncorrectCredentialsException e) {
            e.printStackTrace();
            error = "用户名/密码错误";
            log.error("密码错误");
        } catch (ShiroException e) {
            e.printStackTrace();
            //其他错误，比如锁定，如果想单独处理请单独catch处理
            error = "验证账号密码出现未知错误";
//            error = "其他错误：" + e.getMessage();
        }
        return ResultBuilder.fail(error);
    }


    private String getToken() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId() + "");
        claims.put("username", user.getUsername() + "");
        String jwtToken = jwtUtil.encode(user.getUsername(), tokenExpire * 1000, claims);
        return jwtToken;
    }

    @OperLog(operModul = "用户",operType = Constants.OPER_TYPE_LOGINOUT,operDesc = Constants.OPER_TYPE_LOGINOUT)
    @ApiOperation("退出登录")
    @GetMapping("/logout")
    public Result<String> logout() {
        SecurityUtils.getSubject().logout();
        return ResultBuilder.success();
    }

}
