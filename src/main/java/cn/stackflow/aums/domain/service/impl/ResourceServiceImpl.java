package cn.stackflow.aums.domain.service.impl;

import cn.stackflow.aums.common.bean.PageResult;
import cn.stackflow.aums.common.bean.ResourceDTO;
import cn.stackflow.aums.common.bean.ResourceUiDTO;
import cn.stackflow.aums.common.constant.Constants;
import cn.stackflow.aums.common.utils.StringUtils;
import cn.stackflow.aums.domain.entity.Resource;
import cn.stackflow.aums.domain.entity.Role;
import cn.stackflow.aums.domain.entity.User;
import cn.stackflow.aums.domain.repository.ResourceRepository;
import cn.stackflow.aums.domain.repository.RoleRepository;
import cn.stackflow.aums.domain.repository.UserRepository;
import cn.stackflow.aums.domain.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 资源
 *
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-07-10 10:26
 */
@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    ResourceRepository resourceRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    @Override
    public List<ResourceDTO> getResourceListByUserId(String id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            return null;
        }
        User user = userOptional.get();
        List<String> roleIds = user.getRoleIds();
        List<Role> roleList = roleRepository.findByIdIn(roleIds);

        Set<Resource> resourceSet = new LinkedHashSet<Resource>();
        for (Role role : roleList) {
            resourceSet.addAll(role.getResources());
        }

        //找出1级菜单
        List<ResourceDTO> resourceList = resourceSet.stream()
                .filter(item -> item.getResourceLevel() == Constants.FLAG_TRUE)
                .sorted((s1, s2) -> s1.getSort().compareTo(s2.getSort()))
                .map(this::convertResource)
                .collect(Collectors.toList());

        //找出2级菜单
        resourceList.forEach(item -> {
            if (item.getType() == Resource.ResourceType.GROUP) {
                List<ResourceDTO> collect = resourceSet.stream()
                        .filter(i -> StringUtils.equals(item.getId(), i.getParentId()))
                        .sorted((s1, s2) -> s1.getSort().compareTo(s2.getSort()))
                        .map(this::convertResource)
                        .collect(Collectors.toList());
                item.setChildList(collect);
            }
        });

        //找出三级菜单
        resourceList.forEach(item -> {
            item.getChildList().forEach(childItem -> {
                if (childItem.getType() == Resource.ResourceType.MODULE) {
                    List<ResourceDTO> collect = resourceSet.stream()
                            .filter(i -> StringUtils.equals(childItem.getId(), i.getParentId()))
                            .sorted((s1, s2) -> s1.getSort().compareTo(s2.getSort()))
                            .map(this::convertResource)
                            .collect(Collectors.toList());
                    childItem.setChildList(collect);
                }
            });
        });

        return resourceList;
    }


    @Override
    public List<ResourceDTO> getAllResourceList() {
        List<Resource> list = resourceRepository.findByEnableAndResourceLevelOrderBySortAsc(Constants.FLAG_TRUE, 1);
        return list.stream().map(Resource::getId).map(this::getResource).collect(Collectors.toList());
    }


    @Override
    public List<ResourceUiDTO> getResourceListByRoleId(String roleId) {
        List<Resource> list = resourceRepository.findByEnableAndResourceLevelOrderBySortAsc(Constants.FLAG_TRUE, 1);

        //判断是否用于菜单
        List<String> ids = resourceRepository.findResourceIdsByRoleId(roleId);
        //查询所有菜单
        return list.stream().map(i -> {
            ResourceUiDTO resourceUiDTO = new ResourceUiDTO(i.getId(), i.getName(), ids.contains(i.getId()));
            List<ResourceUiDTO> collect = resourceRepository.findByEnableAndParentIdOrderBySortAsc(1, i.getId()).stream().map(j -> new ResourceUiDTO(j.getId(), j.getName(), ids.contains(i.getId()))).collect(Collectors.toList());
            resourceUiDTO.setChildren(collect);
            return resourceUiDTO;
        }).collect(Collectors.toList());
    }

    private ResourceDTO convertResource(Resource resource) {
        ResourceDTO resourceDTO = new ResourceDTO();
        resourceDTO.setId(resource.getId());
        resourceDTO.setCode(resource.getCode());
        resourceDTO.setName(resource.getName());
        resourceDTO.setRemark(resource.getRemark());
        resourceDTO.setType(resource.getType());
        resourceDTO.setUri(resource.getUri());
        resourceDTO.setIcon(resource.getIcon());
        resourceDTO.setEnable(resource.getEnable());
        resourceDTO.setChildList(new ArrayList<ResourceDTO>());
        return resourceDTO;
    }


    @Override
    public ResourceDTO getResource(String id) {
        Optional<Resource> resourceOptional = resourceRepository.findById(id);
        if (!resourceOptional.isPresent()) {
            return null;
        }
        Resource resource = resourceOptional.get();
        ResourceDTO resourceDTO = convertResource(resource);

        List<ResourceDTO> childResourceList = new ArrayList<ResourceDTO>();

        if (resource.getType() == Resource.ResourceType.GROUP) {
            for (Resource moduleResource : resourceRepository.findByEnableAndParentIdOrderBySortAsc(Constants.FLAG_TRUE, resource.getId())) {
                ResourceDTO moduleResourceDTO = getResource(moduleResource.getId());
                childResourceList.add(moduleResourceDTO);
            }
        }

        if (resource.getType() == Resource.ResourceType.MODULE) {
            for (Resource buttonResource : resourceRepository.findByEnableAndParentIdOrderBySortAsc(Constants.FLAG_TRUE, resource.getId())) {
                ResourceDTO buttonResourceDTO = getResource(buttonResource.getId());
                childResourceList.add(buttonResourceDTO);
            }
        }
        resourceDTO.setChildList(childResourceList);
        return resourceDTO;
    }

    @Override
    public PageResult<ResourceDTO> list(PageResult page, String parentId, String name) {
        PageRequest of = PageRequest.of(page.getPage() - 1, page.getSize(), Sort.by(Sort.Direction.ASC,"sort"));

        Specification<Resource> specification = new Specification<Resource>() {
            @Override
            public javax.persistence.criteria.Predicate toPredicate(Root<Resource> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (StringUtils.isNotEmpty(parentId)) {
                    predicates.add(criteriaBuilder.equal(root.get("parentId"), parentId));
                }
                if (StringUtils.isNotEmpty(name)) {
                    predicates.add(criteriaBuilder.like(root.get("name"), "%"+name+"%"));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };

        Page<Resource> deptPage = resourceRepository.findAll(specification,of);
        page.setTotal(deptPage.getTotalElements());
        page.setRows(deptPage.getContent().stream().map(item -> {
            ResourceDTO resourceDTO = new ResourceDTO();
            resourceDTO.setId(item.getId());
            resourceDTO.setName(item.getName());
            resourceDTO.setRemark(item.getRemark());
            resourceDTO.setType(item.getType());
            resourceDTO.setSort(item.getSort());
            resourceDTO.setUri(item.getUri());
            resourceDTO.setEnable(item.getEnable());
            resourceDTO.setCode(item.getCode());
            resourceDTO.setIcon(item.getIcon());
            resourceDTO.setParentId(item.getParentId());
            if (StringUtils.isNotEmpty(item.getParentId())) {
                String pid = item.getParentId();
                Optional<Resource> resourceOptional = resourceRepository.findById(pid);
                if (resourceOptional.isPresent()) {
                    resourceDTO.setParentName(resourceOptional.get().getName());
                }
            }
            return resourceDTO;
        }).collect(Collectors.toList()));
        return page;
    }

    @Override
    public void create(User user, ResourceDTO resourceDTO) {
        Resource resource = new Resource();
        resource.setParentId(resourceDTO.getParentId());
        resource.setCode(resourceDTO.getCode());
        resource.setName(resourceDTO.getName());
        resource.setRemark(resourceDTO.getRemark());
        resource.setType(resourceDTO.getType());
        resource.setUri(resourceDTO.getUri());
        resource.setIcon(resourceDTO.getIcon());
        resource.setResourceLevel(StringUtils.isEmpty(resourceDTO.getParentId()) ? 1 : 2);
        resource.setSort(0);
        resource.setEnable(resourceDTO.getEnable());
        resourceRepository.save(resource);
    }

    @Override
    public void update(User user, ResourceDTO resourceDTO) {
        Resource resource = resourceRepository.getOne(resourceDTO.getId());
        resource.setParentId(resourceDTO.getParentId());
        resource.setCode(resourceDTO.getCode());
        resource.setName(resourceDTO.getName());
        resource.setRemark(resourceDTO.getRemark());
        resource.setType(resourceDTO.getType());
        resource.setUri(resourceDTO.getUri());
        resource.setIcon(resourceDTO.getIcon());
        resource.setResourceLevel(StringUtils.isEmpty(resourceDTO.getParentId()) ? 1 : 2);
        resource.setSort(0);
        resource.setEnable(resourceDTO.getEnable());
        resourceRepository.save(resource);
    }

    @Override
    public void delete(User user, String id) {
        resourceRepository.deleteById(id);
    }
}
