package cn.stackflow.aums.web.app.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-08-28 10:06
 */
@Getter
@Setter
public class NixSystemLoginDTO extends BaseReq  {

    @NotEmpty
    private String username;//账号
    @NotEmpty
    private String password;//密码
}
