package cn.stackflow.aums.web.app;

import cn.stackflow.aums.common.Result;
import cn.stackflow.aums.common.ResultBuilder;
import cn.stackflow.aums.web.ApiVersion;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-08-27 09:05
 */
@RestController
@RequestMapping(ApiVersion.OPEN_API_VERSION+"/cmd")
public class NixSystemController implements NixSystemClient {



    @PostMapping
    public Result inter(){





        return ResultBuilder.success();
    }


}
