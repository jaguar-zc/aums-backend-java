package cn.stackflow.aums.web.system;

import cn.stackflow.aums.common.Result;
import cn.stackflow.aums.common.ResultBuilder;
import cn.stackflow.aums.common.UserContextHolder;
import cn.stackflow.aums.common.bean.DeptDTO;
import cn.stackflow.aums.common.bean.DeptMemberDTO;
import cn.stackflow.aums.common.bean.PageResult;
import cn.stackflow.aums.domain.entity.User;
import cn.stackflow.aums.domain.service.DeptService;
import cn.stackflow.aums.web.ApiVersion;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-07-06 17:45
 */
@Api("部门")
@RestController
@RequestMapping(ApiVersion.VERSION + "/system/dept")
public class DeptController {

    @Autowired
    DeptService deptService;

    @ApiOperation("部门列表")
    @GetMapping
    public Result<PageResult<DeptDTO>> list(PageResult page) {
        return ResultBuilder.success(deptService.list(page));
    }

    @ApiOperation("部门成员列表")
    @GetMapping("/members")
    public Result<List<DeptMemberDTO>> listMember(@RequestParam(value = "deptId", required = false) String deptId) {
        return ResultBuilder.success(deptService.listMember(deptId));
    }

    @ApiOperation("创建部门")
    @PostMapping
    public Result<String> create(@RequestBody @Valid DeptDTO deptDTO) {
        User user = UserContextHolder.currentUser();
        deptService.create(user, deptDTO);
        return ResultBuilder.success();
    }


    @ApiOperation("修改部门")
    @PutMapping
    public Result<String> update(@RequestBody @Valid DeptDTO deptDTO) {
        User user = UserContextHolder.currentUser();
        deptService.update(user, deptDTO);
        return ResultBuilder.success();
    }

    @ApiOperation("删除部门")
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable("id") String id){
        User user = UserContextHolder.currentUser();
        deptService.delete(user,id);
        return ResultBuilder.success();
    }



}
