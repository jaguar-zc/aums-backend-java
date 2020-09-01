package cn.stackflow.aums.web.app.pojo;

import cn.stackflow.aums.domain.entity.Resource;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-08-28 10:31
 */
@Getter
@Setter
public class NixSystemResourceDTO  {
    private String id;      //id
    private Resource.ResourceType type;//资源类型： 分组、模块、操作
    private String uri;     //URI规则 多个用逗号分开
    private String code;    //权限CODE唯一代码
    private Integer sort;    //权限CODE唯一代码
    private String name;    //资源名称
    private String icon;    //图标
    private List<NixSystemResourceDTO> childList;//子列表

}
