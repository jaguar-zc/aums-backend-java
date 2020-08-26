package cn.stackflow.aums.web.system;

import cn.stackflow.aums.common.Result;
import cn.stackflow.aums.common.ResultBuilder;
import cn.stackflow.aums.common.UserContextHolder;
import cn.stackflow.aums.common.aop.OperLog;
import cn.stackflow.aums.common.bean.*;
import cn.stackflow.aums.common.constant.Constants;
import cn.stackflow.aums.domain.entity.User;
import cn.stackflow.aums.domain.service.UserService;
import cn.stackflow.aums.web.ApiVersion;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-07-02 10:19
 */
@Api("用户")
@RestController
@RequestMapping(ApiVersion.VERSION + "/system/user")
public class UserController {

    @Autowired
    UserService userService;

    @ApiOperation("用户列表")
    @GetMapping
    public Result<PageResult<UserDTO>> list(PageResult page) {
        PageResult<UserDTO> pageResult = userService.list(page);
        return ResultBuilder.success(pageResult);
    }


    @ApiOperation("获取个人信息")
    @GetMapping("/info")
    public Result<UserDTO> info() {
        UserDTO user = userService.getUser(UserContextHolder.currentUser().getId());
        return ResultBuilder.success(user);
    }


    @ApiOperation("根据ID获取用户")
    @GetMapping("/{userId}")
    public Result<UserDTO> get(@PathVariable("userId") String userId) {
        UserDTO user = userService.getUser(userId);
        return ResultBuilder.success(user);
    }

    @OperLog(operModul = "用户",operType = Constants.OPER_TYPE_ADD,operDesc = "添加用户")
    @ApiOperation("添加用户")
    @PostMapping
    public Result<String> add(@RequestBody @Valid UserDTO userDTO) {
        userService.create(userDTO);
        return ResultBuilder.success();
    }

    @OperLog(operModul = "用户",operType = Constants.OPER_TYPE_UPDATE,operDesc = "修改用户")
    @ApiOperation("修改用户")
    @PostMapping
    public Result<String> update(@RequestBody @Valid UserDTO userDTO) {
        userService.update(userDTO);
        return ResultBuilder.success();
    }

    @OperLog(operModul = "用户",operType = Constants.OPER_TYPE_UPDATE,operDesc = "修改手机号")
    @ApiOperation("修改手机号")
    @PutMapping("/updatePhone")
    public Result<String> updatePhone(@RequestBody @Valid UpdatePhoneDTO updatePhoneDTO) {
        userService.updatePhone(updatePhoneDTO);
        return ResultBuilder.success();
    }

    @OperLog(operModul = "用户",operType = Constants.OPER_TYPE_UPDATE,operDesc = "修改密码")
    @ApiOperation("修改密码")
    @PutMapping("/updatePwd")
    public Result<String> updatePwd(@RequestBody @Valid UpdatePwdDTO updatePwdDTO) {
        userService.updatePwd(updatePwdDTO);
        return ResultBuilder.success();
    }

    @OperLog(operModul = "用户",operType = Constants.OPER_TYPE_DELETE,operDesc = "删除用户")
    @ApiOperation("删除用户")
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable("id") String id){
        User user = UserContextHolder.currentUser();
        userService.delete(user,id);
        return ResultBuilder.success();
    }


}
