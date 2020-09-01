package cn.stackflow.aums.web.app;

import cn.stackflow.aums.common.Result;
import cn.stackflow.aums.web.ApiVersion;
import cn.stackflow.aums.web.app.pojo.NixSystemLoginDTO;
import cn.stackflow.aums.web.app.pojo.NixSystemMenuDTO;
import cn.stackflow.aums.web.app.pojo.NixSystemResourceDTO;
import cn.stackflow.aums.web.app.pojo.NixSystemUserDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-08-27 14:50
 */
@RestController
@RequestMapping(ApiVersion.OPEN_API_VERSION+"/client")
public interface NixSystemClient  {

    /**
     * 登录
     * @param login
     * @return
     */
    @RequestMapping("/login")
    Result<NixSystemUserDTO> login(@RequestBody NixSystemLoginDTO login);

    /**
     * 获取菜单
     * @param menuDTO
     * @return
     */
    @RequestMapping("/getMenuList")
    Result<List<NixSystemResourceDTO>> getMenuList(@RequestBody NixSystemMenuDTO menuDTO);
}
