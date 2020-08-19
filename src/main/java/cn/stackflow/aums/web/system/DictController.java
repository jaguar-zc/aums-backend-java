package cn.stackflow.aums.web.system;

import cn.stackflow.aums.common.Result;
import cn.stackflow.aums.common.ResultBuilder;
import cn.stackflow.aums.common.UserContextHolder;
import cn.stackflow.aums.common.bean.DeptDTO;
import cn.stackflow.aums.common.bean.DeptMemberDTO;
import cn.stackflow.aums.common.bean.DictDTO;
import cn.stackflow.aums.common.bean.PageResult;
import cn.stackflow.aums.domain.entity.User;
import cn.stackflow.aums.domain.service.DictService;
import cn.stackflow.aums.web.ApiVersion;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-07-08 09:58
 */
@Api("字典")
@RestController
@RequestMapping(ApiVersion.VERSION + "/system/dict")
public class DictController {


    @Autowired
    DictService dictService;


    @ApiOperation("字典列表")
    @GetMapping
    public Result<PageResult<DictDTO>> list(PageResult page) {
        return ResultBuilder.success(dictService.list(page));
    }


    @ApiOperation("创建字典")
    @PostMapping
    public Result<String> create(@RequestBody @Valid DictDTO deptDTO) {
        User user = UserContextHolder.currentUser();
        dictService.create(user, deptDTO);
        return ResultBuilder.success();
    }


    @ApiOperation("修改字典")
    @PutMapping
    public Result<String> update(@RequestBody @Valid DictDTO deptDTO) {
        User user = UserContextHolder.currentUser();
        dictService.update(deptDTO.getDataCode(),deptDTO.getDataValue());
        return ResultBuilder.success();
    }

    @ApiOperation("删除字典")
    @DeleteMapping("/{code}")
    public Result<String> delete(@PathVariable("code") String code){
        User user = UserContextHolder.currentUser();
        dictService.delete(code);
        return ResultBuilder.success();
    }



}
