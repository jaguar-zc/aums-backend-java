package cn.stackflow.aums.common.bean;

import cn.stackflow.aums.domain.entity.Resource;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-08-25 16:55
 */
@Getter
@Setter
public class ResourceUiDTO {
    private String id;      //id
    private String name;    //资源名称
    private Integer selected;    //是否拥有
    private Resource.ResourceType type;//资源类型： 应用、分组、模块、操作
    private List<ResourceUiDTO> children;//子列表

    public ResourceUiDTO(String id, String name,Boolean checked,Resource.ResourceType type) {
        this.id = id;
        this.name = name;
        this.selected = checked?1:0;
        this.type = type;
    }
}
