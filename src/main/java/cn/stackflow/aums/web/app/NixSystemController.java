package cn.stackflow.aums.web.app;

import cn.stackflow.aums.common.Result;
import cn.stackflow.aums.common.ResultBuilder;
import cn.stackflow.aums.common.bean.ResourceDTO;
import cn.stackflow.aums.common.password.PasswordEncoder;
import cn.stackflow.aums.common.utils.StringUtils;
import cn.stackflow.aums.domain.entity.Dept;
import cn.stackflow.aums.domain.entity.Role;
import cn.stackflow.aums.domain.entity.User;
import cn.stackflow.aums.domain.service.AppService;
import cn.stackflow.aums.domain.service.ResourceService;
import cn.stackflow.aums.domain.service.RoleService;
import cn.stackflow.aums.domain.service.UserService;
import cn.stackflow.aums.web.ApiVersion;
import cn.stackflow.aums.web.app.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-08-27 09:05
 */
@RestController
@RequestMapping(ApiVersion.OPEN_API_VERSION)
public class NixSystemController implements NixSystemClient {


    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ResourceService resourceService;
    @Autowired
    AppService appService;


    /**
     * 登录
     *
     * @param login
     * @return
     */
    @PostMapping("/login")
    @Override
    public Result<NixSystemUserDTO> login(@RequestBody NixSystemLoginDTO login) {
        appService.checkApp(login);

        Optional<User> userOptional = userService.getUserInfoByUsername(login.getUsername());
        if(!userOptional.isPresent()){
            return ResultBuilder.fail("用户不存在");
        }
        User user = userOptional.get();

        if(!StringUtils.equals(passwordEncoder.encode(user.getSalt(),login.getPassword()),user.getPassword())){
            return ResultBuilder.fail("密码错误");
        }

        NixSystemUserDTO userDTO = new NixSystemUserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setName(user.getName());
        userDTO.setPhone(user.getPhone());
        userDTO.setIcon(user.getIcon());
        if(user.getDept() != null){
            Dept dept = user.getDept();
            NixSystemDeptDTO nixSystemDeptDTO = new NixSystemDeptDTO();
            nixSystemDeptDTO.setId(dept.getId());
            nixSystemDeptDTO.setName(dept.getName());
            nixSystemDeptDTO.setRemark(dept.getRemark());
            userDTO.setDept(nixSystemDeptDTO);
        }
        userDTO.setEnable(user.getEnable());

        List<NixSystemRoleDTO> roleList  = new ArrayList<NixSystemRoleDTO>();
        for (Role role : roleService.findRoleByIds(user.getRoleIds())) {
            NixSystemRoleDTO roleDTO  = new NixSystemRoleDTO();
            roleDTO.setId(role.getId());
            roleDTO.setName(role.getName());
            roleDTO.setCode(role.getCode());
            roleList.add(roleDTO);
        }
        userDTO.setRoleList(roleList);
        return ResultBuilder.success(userDTO);
    }

    /**
     * 获取菜单
     *
     * @param menuDTO
     * @return
     */
    @PostMapping("/getMenuList")
    @Override
    public Result<List<NixSystemResourceDTO>> getMenuList(@RequestBody NixSystemMenuDTO menuDTO) {
        appService.checkApp(menuDTO);
        User user = userService.get(menuDTO.getUserId());
        List<ResourceDTO> list = resourceService.getResourceListByRoleIdAndAppID(user.getRoleIds(), menuDTO.getAppId());
        List<NixSystemResourceDTO> collect = list.stream()
                .map(this::convert).collect(Collectors.toList());
        return ResultBuilder.success(collect);
    }

    private NixSystemResourceDTO convert(ResourceDTO resourceDTO){
        NixSystemResourceDTO nixSystemResource = new NixSystemResourceDTO();
        nixSystemResource.setId(resourceDTO.getId());
        nixSystemResource.setType(resourceDTO.getType());
        nixSystemResource.setUri(resourceDTO.getUri());
        nixSystemResource.setCode(resourceDTO.getCode());
        nixSystemResource.setSort(resourceDTO.getSort());
        nixSystemResource.setName(resourceDTO.getName());
        nixSystemResource.setIcon(resourceDTO.getIcon());
        List<NixSystemResourceDTO> childList = new ArrayList<NixSystemResourceDTO>();
        for (ResourceDTO dto : resourceDTO.getChildList()) {
            childList.add(convert(dto));
        }
        nixSystemResource.setChildList(childList);
        return nixSystemResource;
    }
}
