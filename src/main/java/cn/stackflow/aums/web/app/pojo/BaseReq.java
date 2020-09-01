package cn.stackflow.aums.web.app.pojo;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-08-28 10:03
 */
public class BaseReq {
    @NotEmpty
    private String appId;
    @NotEmpty
    private String secret;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
