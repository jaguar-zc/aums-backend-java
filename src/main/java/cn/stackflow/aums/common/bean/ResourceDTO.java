package cn.stackflow.aums.common.bean;

import cn.stackflow.aums.domain.entity.Resource;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-07-10 10:05
 */
@Getter
@Setter
public class ResourceDTO {

    private String id;      //id
    @JsonIgnore
    private String remark;  //资源描述
    @JsonIgnore
    private Resource.ResourceType type;//资源类型： 分组、模块、操作
    @JsonIgnore
    private String uri;     //URI规则 多个用逗号分开
    @JsonIgnore
    private Integer enable; //1启用;0停用
    private String code;    //权限CODE唯一代码
    private String name;    //资源名称
    private String icon;    //图标
    private List<ResourceDTO> childList;//子列表

}
