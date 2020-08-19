package cn.stackflow.aums.domain.service;

import cn.stackflow.aums.common.bean.PageResult;
import cn.stackflow.aums.common.bean.RoleDTO;
import cn.stackflow.aums.domain.entity.Role;
import cn.stackflow.aums.domain.entity.User;

import java.util.List;

public interface RoleService {

    List<Role> findRoleByIds(List<String> ids);

    PageResult<RoleDTO> list(PageResult page);

    void update(User user, RoleDTO roleDTO);

    void add(User user, RoleDTO roleDTO);

    void delete(User user, String id);
}
