package cn.stackflow.aums.common.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-07-11 15:27
 */

public class UsernamePasswordJWTToken extends UsernamePasswordToken {
    private static final long serialVersionUID = -6782847141732554573L;
    private boolean jwt;

    public UsernamePasswordJWTToken(String username, String password, boolean jwt) {
        super(username, password);
        this.jwt = jwt;
    }

    public boolean isJwt() {
        return jwt;
    }

    public void setJwt(boolean jwt) {
        this.jwt = jwt;
    }
}
