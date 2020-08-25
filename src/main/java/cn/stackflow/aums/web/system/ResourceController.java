package cn.stackflow.aums.web.system;

import cn.stackflow.aums.common.Result;
import cn.stackflow.aums.common.ResultBuilder;
import cn.stackflow.aums.common.UserContextHolder;
import cn.stackflow.aums.common.bean.MenuType;
import cn.stackflow.aums.common.bean.PageResult;
import cn.stackflow.aums.common.bean.ResourceDTO;
import cn.stackflow.aums.common.bean.ResourceMenuDTO;
import cn.stackflow.aums.domain.entity.User;
import cn.stackflow.aums.domain.service.ResourceService;
import cn.stackflow.aums.web.ApiVersion;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-07-10 10:04
 */
@Api("资源")
@RestController
@RequestMapping(ApiVersion.VERSION + "/system/resource")
public class ResourceController {

    @Autowired
    ResourceService resourceService;

    @ApiOperation("菜单列表")
    @GetMapping("/menu")
    public Result<List<ResourceMenuDTO>> getMenu(MenuType menuType){
        User user = UserContextHolder.currentUser();
        if(menuType == MenuType.ALL){
            return ResultBuilder.success(ResourceMenuDTO.convert(resourceService.getAllResourceList()));
        }
        if(menuType == MenuType.ME){
            return ResultBuilder.success(ResourceMenuDTO.convert(resourceService.getResourceListByUserId(user.getId())));
        }
        return ResultBuilder.fail("不支持的类型");
    }

    @ApiOperation("根据角色ID获取菜单列表")
    @GetMapping("/role/{roleId}")
    public Result<List<ResourceMenuDTO>> getResourceListByRoleId(@PathVariable("roleId") String roleId){
        User user = UserContextHolder.currentUser();
        return ResultBuilder.success(ResourceMenuDTO.convert(resourceService.getResourceListByRoleId(roleId)));
    }



    @ApiOperation("资源列表")
    @GetMapping
    public Result<PageResult<ResourceDTO>> list(PageResult page) {
        return ResultBuilder.success(resourceService.list(page));
    }

    @ApiOperation("创建资源")
    @PostMapping
    public Result<String> create(@RequestBody @Valid ResourceDTO resourceDTO) {
        User user = UserContextHolder.currentUser();
        resourceService.create(user, resourceDTO);
        return ResultBuilder.success();
    }


    @ApiOperation("修改资源")
    @PutMapping
    public Result<String> update(@RequestBody @Valid ResourceDTO resourceDTO) {
        User user = UserContextHolder.currentUser();
        resourceService.update(user, resourceDTO);
        return ResultBuilder.success();
    }

    @ApiOperation("删除资源")
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable("id") String id){
        User user = UserContextHolder.currentUser();
        resourceService.delete(user,id);
        return ResultBuilder.success();
    }





}
