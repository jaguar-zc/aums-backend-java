package cn.stackflow.aums.common.bean;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-07-05 10:33
 */
@Getter
@Setter
public class RoleDTO {

    private String id;
    private String appId;//应用Id
    @NotEmpty
    private String name;//角色名
    private String code;//角色编码
    private String remark;//描述
    private List<ResourceDTO> resourceList;


}
