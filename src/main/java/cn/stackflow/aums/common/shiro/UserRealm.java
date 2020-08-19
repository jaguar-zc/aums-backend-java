package cn.stackflow.aums.common.shiro;

import cn.stackflow.aums.domain.entity.Resource;
import cn.stackflow.aums.domain.entity.User;
import cn.stackflow.aums.domain.service.RoleService;
import cn.stackflow.aums.common.password.PasswordEncoder;
import cn.stackflow.aums.common.utils.SpringUtils;
import cn.stackflow.aums.common.utils.StringUtils;
import cn.stackflow.aums.domain.entity.Role;
import cn.stackflow.aums.domain.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-06-18 10:29
 */
@Slf4j
public class UserRealm extends AuthorizingRealm {

    @Override
    public boolean supports(AuthenticationToken token) {
        //这个token就是从过滤器中传入的jwtToken
        return token instanceof UsernamePasswordJWTToken;
    }

    /**
     * 授权
     *
     * @see SimpleAuthorizationInfo
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        RoleService roleService = SpringUtils.getBean(RoleService.class);
        //获取登录用户名
        User user = (User) principals.getPrimaryPrincipal();
        //添加角色和权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        List<String> roleIds = Arrays.asList(user.getRoles().split(User.USER_ROLE_SEGMENT));

        for (Role role : roleService.findRoleByIds(roleIds)) {
            //添加角色
            simpleAuthorizationInfo.addRole(role.getName());
            //添加权限
            for (Resource resource : role.getResources()) {
                simpleAuthorizationInfo.addStringPermission(resource.getCode());
            }
        }
        return simpleAuthorizationInfo;
    }

    /**
     * 认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordJWTToken upToken = (UsernamePasswordJWTToken) token;
        if (log.isTraceEnabled()) {
            log.trace("UserRealm.doGetAuthenticationInfo:{}", upToken.getUsername() + ":" + upToken.getPassword());
        }
        UserService userService = SpringUtils.getBean(UserService.class);
        PasswordEncoder passwordEncoder = SpringUtils.getBean(PasswordEncoder.class);
        Optional<User> userInfoOptional = userService.getUserInfoByUsername(upToken.getUsername());
        if (!userInfoOptional.isPresent()) {
            throw new UnknownAccountException(); //如果用户名错误
        }
        User userInfo = userInfoOptional.get();
        if (!upToken.isJwt()) {
            String encodeInputPassword = passwordEncoder.encode(userInfo.getSalt(), new String(upToken.getPassword()));
            if (!StringUtils.equals(userInfo.getPassword(), encodeInputPassword)) {
                throw new IncorrectCredentialsException(); //如果密码错误
            }
        }
        //如果身份认证验证成功，返回一个AuthenticationInfo实现；
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(userInfo, ((UsernamePasswordToken) token).getPassword(), getName());
        return simpleAuthenticationInfo;
    }

}
