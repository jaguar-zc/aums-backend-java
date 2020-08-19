package cn.stackflow.aums.common.bean;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-07-05 10:33
 */
@Getter
@Setter
public class UserDTO {

    private String id;
    @NotEmpty
    @Pattern(regexp = "^[A-Za-z0-9_-]{6,20}$", message = "账号仅允许数字字母下划线6-20位")
    private String username;//账号
    @NotEmpty
    private String password;//密码
    @NotEmpty
    private String name;//姓名
    @NotEmpty
    private String phone;//手机号
    private String icon;//图标
    @NotEmpty
    private String deptId;//部门
    private String deptName;//部门
    private Integer enable;// 0:禁用 1:启用
    @NotEmpty
    private List<RoleDTO> roleList;//角色
}
