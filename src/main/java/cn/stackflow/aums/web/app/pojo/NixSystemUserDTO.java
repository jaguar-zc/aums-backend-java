package cn.stackflow.aums.web.app.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-08-28 10:06
 */
@Getter
@Setter
public class NixSystemUserDTO   {

    private String id;
    private String username;//账号
    private String name;//姓名
    private String phone;//手机号
    private String icon;//图标
    private NixSystemDeptDTO dept;//部门
    private Integer enable;// 0:禁用 1:启用
    private List<NixSystemRoleDTO> roleList;//角色

}
