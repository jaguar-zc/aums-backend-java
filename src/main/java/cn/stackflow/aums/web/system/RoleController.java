package cn.stackflow.aums.web.system;

import cn.stackflow.aums.domain.service.RoleService;
import cn.stackflow.aums.common.Result;
import cn.stackflow.aums.common.ResultBuilder;
import cn.stackflow.aums.common.UserContextHolder;
import cn.stackflow.aums.common.bean.PageResult;
import cn.stackflow.aums.common.bean.RoleDTO;
import cn.stackflow.aums.domain.entity.User;
import cn.stackflow.aums.web.ApiVersion;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-07-06 17:45
 */
@Api("角色")
@RestController
@RequestMapping(ApiVersion.VERSION + "/system/role")
public class RoleController {

    @Autowired
    RoleService roleService;

    @ApiOperation("角色列表")
    @GetMapping
    public Result<PageResult<RoleDTO>> list(PageResult page){
        return ResultBuilder.success(roleService.list(page));
    }

    @ApiOperation("添加角色")
    @PostMapping
    public Result<String> add(@RequestBody @Valid RoleDTO roleDTO){
        User user = UserContextHolder.currentUser();
        roleService.add(user,roleDTO);
        return ResultBuilder.success();
    }


    @ApiOperation("修改角色")
    @PutMapping
    public Result<String> update(@RequestBody @Valid RoleDTO roleDTO){
        User user = UserContextHolder.currentUser();
        roleService.update(user,roleDTO);
        return ResultBuilder.success();
    }


    @ApiOperation("删除角色")
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable("id") String id){
        User user = UserContextHolder.currentUser();
        roleService.delete(user,id);
        return ResultBuilder.success();
    }


}
