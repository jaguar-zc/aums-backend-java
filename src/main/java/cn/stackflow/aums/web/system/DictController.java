package cn.stackflow.aums.web.system;

import cn.stackflow.aums.common.Result;
import cn.stackflow.aums.common.ResultBuilder;
import cn.stackflow.aums.common.UserContextHolder;
import cn.stackflow.aums.common.aop.OperLog;
import cn.stackflow.aums.common.bean.DictTypeDTO;
import cn.stackflow.aums.common.bean.DictValueDTO;
import cn.stackflow.aums.common.bean.PageResult;
import cn.stackflow.aums.common.constant.Constants;
import cn.stackflow.aums.domain.entity.User;
import cn.stackflow.aums.domain.service.DictService;
import cn.stackflow.aums.web.ApiVersion;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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


    @ApiOperation("字典类型列表")
    @GetMapping("/type_list")
    public Result<PageResult<DictTypeDTO>> getTypeList(PageResult page) {
        return ResultBuilder.success(dictService.getTypeList(page));
    }

    @ApiOperation("字典列表")
    @GetMapping("/value_list")
    public Result<PageResult<DictValueDTO>> getValueList(PageResult page,
                                                         @RequestParam(value = "dictTypeId", required = false) String dictTypeId) {
        return ResultBuilder.success(dictService.getValueList(page,dictTypeId));
    }



    @OperLog(operModul = "字典类型",operType = Constants.OPER_TYPE_ADD,operDesc = "创建字典类型")
    @ApiOperation("创建字典类型")
    @PostMapping("/type")
    public Result<String> createDictType(@RequestBody @Valid DictTypeDTO dictType) {
        User user = UserContextHolder.currentUser();
        dictService.createDictType(user, dictType);
        return ResultBuilder.success();
    }

    @OperLog(operModul = "字典",operType = Constants.OPER_TYPE_ADD,operDesc = "创建字典")
    @ApiOperation("创建字典")
    @PostMapping("/value")
    public Result<String> createDictValue(@RequestBody @Valid DictValueDTO deptDTO) {
        User user = UserContextHolder.currentUser();
        dictService.createDictValue(user,deptDTO);
        return ResultBuilder.success();
    }

    @OperLog(operModul = "字典类型",operType = Constants.OPER_TYPE_UPDATE,operDesc = "修改字典类型")
    @ApiOperation("修改字典类型")
    @PutMapping
    public Result<String> updateDictType(@RequestBody @Valid DictTypeDTO dictType) {
        dictService.updateDictType(dictType);
        return ResultBuilder.success();
    }

    @OperLog(operModul = "字典类型",operType = Constants.OPER_TYPE_DELETE,operDesc = "删除字典类型")
    @ApiOperation("删除字典类型")
    @DeleteMapping("/type/{id}")
    public Result<String> deleteDictType(@PathVariable("id") String id){
        dictService.deleteDictType(id);
        return ResultBuilder.success();
    }

    @OperLog(operModul = "字典",operType = Constants.OPER_TYPE_DELETE,operDesc = "删除字典")
    @ApiOperation("删除字典")
    @DeleteMapping("/value/{id}")
    public Result<String> deleteDictValue(@PathVariable("id") String id){
        dictService.deleteDictValue(id);
        return ResultBuilder.success();
    }

}
