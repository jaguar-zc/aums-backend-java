package cn.stackflow.aums.common.bean;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-07-11 14:19
 */
@Getter
@Setter
public class UpdatePwdDTO {
    @NotEmpty
    private String userId;
    @NotEmpty
    private String newPassword;

}
