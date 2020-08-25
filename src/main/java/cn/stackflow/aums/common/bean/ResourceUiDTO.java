package cn.stackflow.aums.common.bean;

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
    private List<ResourceUiDTO> children;//子列表

    public ResourceUiDTO(String id, String name,Boolean checked) {
        this.id = id;
        this.name = name;
        this.selected = checked?1:0;
    }
}
