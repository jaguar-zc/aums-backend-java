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
import org.springframework.util.Assert;

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
    public List<ResourceDTO> getResourceListByUserId(String userId) {
        Optional<User> userOptional = userRepository.findById(userId);
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
                .filter(item -> item.getResourceLevel() == Constants.RESOURCE_LEVEL_ROOT)
                .sorted((s1, s2) -> s1.getSort().compareTo(s2.getSort()))
                .map(this::convertResource)
                .collect(Collectors.toList());
        for (ResourceDTO resourceDTO : resourceList) {
            resourceDTO.setChildList(getResourceAndChildren(resourceDTO.getId()));
        }
        return resourceList;
    }



    private List<ResourceDTO> getResourceAndChildren(String resourceId){
        List<Resource> rootList = new ArrayList<Resource>();
        if(StringUtils.isEmpty(resourceId)){
            rootList = resourceRepository.findByEnableAndResourceLevelOrderBySortAsc(Constants.FLAG_TRUE, 0);
        }else{
            rootList = resourceRepository.findByEnableAndParentIdOrderBySortAsc(Constants.FLAG_TRUE, resourceId);
        }
        return rootList.stream().map(item -> {
            ResourceDTO resourceDTO = convertResource(item);
            resourceDTO.setChildList(getResourceAndChildren(item.getId()));
            return resourceDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ResourceUiDTO> getResourceListByRoleId(String roleId) {
        //查询角色拥有的资源，判断是否用于菜单
        List<String> ids = resourceRepository.findResourceIdsByRoleId(roleId);
        return  getChildren(ids,null);
    }

    private List<ResourceUiDTO> getChildren(List<String> ids,String resourceId){
        List<Resource> rootList = new ArrayList<Resource>();
        if(StringUtils.isEmpty(resourceId)){
            rootList = resourceRepository.findByEnableAndResourceLevelOrderBySortAsc(Constants.FLAG_TRUE, 0);
        }else{
            rootList = resourceRepository.findByEnableAndParentIdOrderBySortAsc(Constants.FLAG_TRUE, resourceId);
        }
        return rootList.stream().map(item -> {
            ResourceUiDTO resourceUiDTO = new ResourceUiDTO(item.getId(), item.getName(), ids.contains(item.getId()),item.getType());
            resourceUiDTO.setChildren(getChildren(ids,item.getId()));
            return resourceUiDTO;
        }).collect(Collectors.toList());
    }

    private ResourceDTO convertResource(Resource resource) {
        ResourceDTO resourceDTO = new ResourceDTO();
        resourceDTO.setId(resource.getId());
        resourceDTO.setAppId(resource.getAppId());
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
        for (Resource moduleResource : resourceRepository.findByEnableAndParentIdOrderBySortAsc(Constants.FLAG_TRUE, resource.getId())) {
            ResourceDTO moduleResourceDTO = getResource(moduleResource.getId());
            childResourceList.add(moduleResourceDTO);
        }
        resourceDTO.setChildList(childResourceList);
        return resourceDTO;
    }

    @Override
    public PageResult<ResourceDTO> list(PageResult page, String appId, String name) {
        PageRequest of = PageRequest.of(page.getPage() - 1, page.getSize(), Sort.by(Sort.Direction.ASC,"sort"));

        Specification<Resource> specification = new Specification<Resource>() {
            @Override
            public javax.persistence.criteria.Predicate toPredicate(Root<Resource> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.equal(root.get("resourceLevel"), 0));
                if (StringUtils.isNotEmpty(appId)) {
                    predicates.add(criteriaBuilder.equal(root.get("appId"), appId));
                }
                if (StringUtils.isNotEmpty(name)) {
                    predicates.add(criteriaBuilder.like(root.get("name"), "%"+name+"%"));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        Page<Resource> deptPage = resourceRepository.findAll(specification,of);
        page.setTotal(deptPage.getTotalElements());
        page.setRows(deptPage.getContent().stream().map(Resource::getId).map(this::getResource).collect(Collectors.toList()));
        return page;
    }

    private ResourceDTO convertToResourceDTO(Resource item){
        ResourceDTO resourceDTO = new ResourceDTO();
        resourceDTO.setId(item.getId());
        resourceDTO.setAppId(item.getAppId());
        resourceDTO.setResourceLevel(item.getResourceLevel());
        resourceDTO.setName(item.getName());
        resourceDTO.setRemark(item.getRemark());
        resourceDTO.setType(item.getType());
        resourceDTO.setSort(item.getSort());
        resourceDTO.setUri(item.getUri());
        resourceDTO.setEnable(item.getEnable());
        resourceDTO.setCode(item.getCode());
        resourceDTO.setIcon(item.getIcon());
        resourceDTO.setParentId(item.getParentId());
        return resourceDTO;
    }

    @Override
    public void create(User user, ResourceDTO resourceDTO) {
        Resource resource = new Resource();
        resource.setAppId(resourceDTO.getAppId());
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
        resource.setAppId(resourceDTO.getAppId());
        resource.setParentId(resourceDTO.getParentId());
        resource.setCode(resourceDTO.getCode());
        resource.setName(resourceDTO.getName());
        resource.setRemark(resourceDTO.getRemark());
        resource.setType(resourceDTO.getType());
        resource.setUri(resourceDTO.getUri());
        resource.setIcon(resourceDTO.getIcon());
        resource.setResourceLevel(StringUtils.isEmpty(resourceDTO.getParentId()) ? 1 : 2);
        resource.setSort(resourceDTO.getSort());
        resource.setEnable(resourceDTO.getEnable());
        resourceRepository.save(resource);
    }

    @Override
    public void delete(User user, String id) {
        Resource resource = resourceRepository.getOne(id);
        if(resource.getType() == Resource.ResourceType.APP){
            Assert.isTrue(false,"不能删除根节点");
        }

        resourceRepository.deleteById(id);
    }

    @Override
    public List<ResourceDTO> listLazy(String parentId) {
        List<ResourceDTO> childResourceList = new ArrayList<ResourceDTO>();

        List<Resource> resourceList = new ArrayList<Resource>();
        if (StringUtils.isEmpty(parentId)) {
            resourceList = resourceRepository.findByEnableAndResourceLevelOrderBySortAsc(Constants.FLAG_TRUE, 0);
        }else{
            resourceList = resourceRepository.findByEnableAndParentIdOrderBySortAsc(Constants.FLAG_TRUE, parentId);
        }
        for (Resource moduleResource : resourceList) {
            ResourceDTO moduleResourceDTO = convertToResourceDTO(moduleResource);
            moduleResourceDTO.setLeaf(resourceRepository.countByParentId(moduleResource.getId()) == 0);
            childResourceList.add(moduleResourceDTO);
        }
        return childResourceList;
    }

    @Override
    public List<ResourceDTO> getResourceListByRoleIdAndAppID(List<String> roleId, String appId) {
        List<ResourceDTO> childList  = new ArrayList<ResourceDTO>();
        Set<String> setIds = new HashSet<String>();
        for (String id : roleId) {
            setIds.addAll(resourceRepository.findResourceIdsByRoleId(id));
        }

        Optional<Resource> resourceOptional = resourceRepository.findByTypeAndAppId(Resource.ResourceType.APP, appId);
        if (!resourceOptional.isPresent() || !setIds.contains(resourceOptional.get().getId())) {
            return childList;
        }
        Resource resource = resourceOptional.get();

        ResourceDTO resource1 = getResource(resource.getId());
        if(resource1 == null){
            return childList;
        }
        return resource1.getChildList();
    }
}
