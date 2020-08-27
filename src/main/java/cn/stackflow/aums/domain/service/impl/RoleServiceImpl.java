package cn.stackflow.aums.domain.service.impl;

import cn.stackflow.aums.common.bean.PageResult;
import cn.stackflow.aums.common.bean.ResourceDTO;
import cn.stackflow.aums.common.bean.RoleDTO;
import cn.stackflow.aums.common.exception.ServiceException;
import cn.stackflow.aums.common.utils.JSONUtils;
import cn.stackflow.aums.domain.entity.Resource;
import cn.stackflow.aums.domain.entity.Role;
import cn.stackflow.aums.domain.entity.User;
import cn.stackflow.aums.domain.repository.ResourceRepository;
import cn.stackflow.aums.domain.repository.RoleRepository;
import cn.stackflow.aums.domain.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ResourceRepository resourceRepository;


    @Override
    public List<Role> findRoleByIds(List<String> ids) {
        return roleRepository.findByIdIn(ids);
    }


    @Override
    public PageResult<RoleDTO> list(PageResult page) {
        Page<Role> deptPage = roleRepository.findAll(PageRequest.of(page.getPage() - 1, page.getSize()));
        page.setTotal(deptPage.getTotalElements());
        List<RoleDTO> collect = deptPage.getContent()
                .stream()
                .map(item -> {
                    RoleDTO roleDTO = new RoleDTO();
                    roleDTO.setId(item.getId());
                    roleDTO.setName(item.getName());
                    roleDTO.setCode(item.getCode());
                    roleDTO.setRemark(item.getRemark());
                    List<ResourceDTO> collect1 = item.getResources()
                            .stream()
                            .filter(i -> i.getResourceLevel() == 1).map(i -> {
                                ResourceDTO resourceDTO = new ResourceDTO();
                                resourceDTO.setId(i.getId());
                                resourceDTO.setAppId(i.getAppId());
                                resourceDTO.setCode(i.getCode());
                                resourceDTO.setName(i.getName());
                                resourceDTO.setRemark(i.getRemark());
                                resourceDTO.setType(i.getType());
                                resourceDTO.setUri(i.getUri());
                                resourceDTO.setIcon(i.getIcon());
                                resourceDTO.setEnable(i.getEnable());
                                resourceDTO.setChildList(new ArrayList<ResourceDTO>());
                                return resourceDTO;
                            }).collect(Collectors.toList());

                    roleDTO.setResourceList(collect1);
                    return roleDTO;
                }).collect(Collectors.toList());
        page.setRows(collect);
        return page;
    }

    @Override
    public void add(User user, RoleDTO roleDTO) {
        Role role = new Role();
        role.setAppId(roleDTO.getAppId());
        role.setName(roleDTO.getName());
        role.setCode(roleDTO.getCode());
        role.setRemark(roleDTO.getRemark());
        Set<Resource> collect = roleDTO.getResourceList().stream().map(ResourceDTO::getId).map(resourceRepository::getOne).collect(Collectors.toSet());
        role.setResources(collect);
        roleRepository.save(role);
    }


    @Override
    public void update(User user, RoleDTO roleDTO) {
        log.info("update:{}", JSONUtils.object2Json(roleDTO));
        Optional<Role> roleOptional = roleRepository.findById(roleDTO.getId());
        if (!roleOptional.isPresent()) {
            throw new ServiceException("角色不存在");
        }
        Role role = roleOptional.get();
        role.setAppId(roleDTO.getAppId());
        role.setRemark(roleDTO.getRemark());
        Set<Resource> resources = role.getResources();
        resources.clear();
        for (ResourceDTO resourceDTO : roleDTO.getResourceList()) {
            Resource r = resourceRepository.getOne(resourceDTO.getId());
            resources.add(r);
        }
        role.setResources(resources);
        roleRepository.save(role);
    }

    @Override
    public void delete(User user, String id) {
        Optional<Role> roleOptional = roleRepository.findById(id);
        if (roleOptional.isPresent()) {
            Role role = roleOptional.get();
            roleRepository.delete(role);
        }
    }


}
