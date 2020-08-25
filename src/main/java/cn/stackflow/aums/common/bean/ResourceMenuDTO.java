package cn.stackflow.aums.common.bean;
import com.google.common.collect.Lists;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-07-10 10:05
 */
@Getter
@Setter
public class ResourceMenuDTO {

    private String id;      //id
    private String key;    //权限CODE唯一代码
    private String title;    //资源名称
    private Integer checked;    //是否拥有
    private String icon;    //图标
    private List<ResourceMenuDTO> children;//子列表



    public static List<ResourceMenuDTO> convert(List<ResourceDTO> resourceDTO){
        List<ResourceMenuDTO> list = new ArrayList<ResourceMenuDTO>();
        for (ResourceDTO dto : resourceDTO) {
            ResourceMenuDTO resourceMenuDTO = new ResourceMenuDTO();
            resourceMenuDTO.setId(dto.getId());
            resourceMenuDTO.setKey(dto.getCode());
            resourceMenuDTO.setTitle(dto.getName());
            resourceMenuDTO.setIcon(dto.getIcon());
            resourceMenuDTO.setChildren(Lists.newArrayList());
            List<ResourceMenuDTO> childList = new ArrayList<ResourceMenuDTO>();
            for (ResourceDTO child : dto.getChildList()) {
                ResourceMenuDTO childDTO = new ResourceMenuDTO();
                childDTO.setId(child.getId());
                childDTO.setKey(child.getCode());
                childDTO.setTitle(child.getName());
                childDTO.setIcon(child.getIcon());
                childList.add(childDTO);
            }
            resourceMenuDTO.setChildren(childList);
            list.add(resourceMenuDTO);
        }

        return list;
    }

}
