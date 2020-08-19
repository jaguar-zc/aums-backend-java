package cn.stackflow.aums.common.bean;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-07-03 10:21
 */
@Getter
@Setter
public class LoginReq {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
}
