package cn.stackflow.aums.common;

import cn.stackflow.aums.domain.entity.User;
import org.apache.shiro.SecurityUtils;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-07-03 14:21
 */
public final class UserContextHolder {


    public static User currentUser() {
        return (User) SecurityUtils.getSubject().getPrincipal();
    }


}
