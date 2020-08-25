package cn.stackflow.aums.domain.service;

import cn.stackflow.aums.common.bean.PageResult;
import cn.stackflow.aums.common.bean.ResourceDTO;
import cn.stackflow.aums.domain.entity.User;

import java.util.List;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-07-10 10:04
 */
public interface ResourceService {

    List<ResourceDTO> getResourceListByUserId(String id);

    List<ResourceDTO> getAllResourceList();

    ResourceDTO getResource(String id);

    PageResult<ResourceDTO> list(PageResult page);

    void create(User user, ResourceDTO resourceDTO);

    void update(User user, ResourceDTO resourceDTO);

    void delete(User user, String id);

    List<ResourceDTO> getResourceListByRoleId(String roleId);
}
