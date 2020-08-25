package cn.stackflow.aums.web.system;

import cn.stackflow.aums.common.Result;
import cn.stackflow.aums.common.ResultBuilder;
import cn.stackflow.aums.common.UserContextHolder;
import cn.stackflow.aums.common.aop.OperLog;
import cn.stackflow.aums.common.bean.DeptDTO;
import cn.stackflow.aums.common.bean.DeptMemberDTO;
import cn.stackflow.aums.common.bean.PageResult;
import cn.stackflow.aums.common.constant.Constants;
import cn.stackflow.aums.domain.entity.OperLogs;
import cn.stackflow.aums.domain.entity.User;
import cn.stackflow.aums.domain.service.DeptService;
import cn.stackflow.aums.domain.service.OperLogsService;
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
@Api("日志")
@RestController
@RequestMapping(ApiVersion.VERSION + "/system/logs")
public class OperLogsController {

    @Autowired
    OperLogsService operLogsService;

    @ApiOperation("日志列表")
    @GetMapping
    public Result<PageResult<OperLogs>> list(PageResult page,
                                             @RequestParam(value = "logsType",required = false) Integer logsType) {
        return ResultBuilder.success(operLogsService.findList(page,logsType));
    }

}
