package cn.stackflow.aums.common.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-07-03 10:21
 */
@Getter
@Setter
public class LoginResp {
    private String token;

    public LoginResp(String token) {
        this.token = token;
    }
}
