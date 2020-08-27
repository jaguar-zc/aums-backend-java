package cn.stackflow.aums.web.system;

import cn.stackflow.aums.common.Result;
import cn.stackflow.aums.common.ResultBuilder;
import cn.stackflow.aums.common.UserContextHolder;
import cn.stackflow.aums.common.aop.OperLog;
import cn.stackflow.aums.common.bean.DeptDTO;
import cn.stackflow.aums.common.bean.DeptMemberDTO;
import cn.stackflow.aums.common.bean.PageResult;
import cn.stackflow.aums.common.constant.Constants;
import cn.stackflow.aums.domain.entity.App;
import cn.stackflow.aums.domain.entity.User;
import cn.stackflow.aums.domain.service.AppService;
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
@Api("应用")
@RestController
@RequestMapping(ApiVersion.VERSION + "/system/app")
public class AppController {

    @Autowired
    AppService appService;

    @ApiOperation("应用列表")
    @GetMapping
    public Result<PageResult<App>> list(PageResult page) {
        return ResultBuilder.success(appService.list(page));
    }

    @OperLog(operModul = "应用", operType = Constants.OPER_TYPE_ADD, operDesc = "创建应用")
    @ApiOperation("创建应用")
    @PostMapping
    public Result<String> create(@RequestBody @Valid App app) {
        appService.create(app);
        return ResultBuilder.success();
    }

    @OperLog(operModul = "应用", operType = Constants.OPER_TYPE_UPDATE, operDesc = "修改应用")
    @ApiOperation("修改应用")
    @PutMapping
    public Result<String> update(@RequestBody @Valid App app) {
        appService.update(app);
        return ResultBuilder.success();
    }

    @OperLog(operModul = "应用", operType = Constants.OPER_TYPE_DELETE, operDesc = "删除应用")
    @ApiOperation("删除应用")
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable("id") String id) {
        appService.delete(id);
        return ResultBuilder.success();
    }


}
