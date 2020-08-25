package cn.stackflow.aums.web.system;

import cn.stackflow.aums.common.Result;
import cn.stackflow.aums.common.ResultBuilder;
import cn.stackflow.aums.common.UserContextHolder;
import cn.stackflow.aums.common.aop.OperLog;
import cn.stackflow.aums.common.bean.DeptDTO;
import cn.stackflow.aums.common.bean.DeptMemberDTO;
import cn.stackflow.aums.common.bean.PageResult;
import cn.stackflow.aums.common.constant.Constants;
import cn.stackflow.aums.common.utils.JVMResource;
import cn.stackflow.aums.domain.entity.User;
import cn.stackflow.aums.domain.service.DeptService;
import cn.stackflow.aums.web.ApiVersion;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-07-06 17:45
 */
@Api("JVM")
@RestController
@RequestMapping(ApiVersion.VERSION + "/system/jvm")
public class JvmController {

    @Autowired
    DeptService deptService;

    @ApiOperation("监控")
    @GetMapping("/monitor")
    public Result<Map<String, Object>> list() {
        return ResultBuilder.success(JVMResource.getSummary());
    }



}
