package cn.stackflow.aums.domain.service;

import cn.stackflow.aums.common.bean.PageResult;
import cn.stackflow.aums.domain.entity.App;
import cn.stackflow.aums.web.app.pojo.BaseReq;
import cn.stackflow.aums.web.app.pojo.NixSystemLoginDTO;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-08-27 09:20
 */
public interface AppService {

    PageResult<App> list(PageResult page);

    void create(App app);

    void update(App app);

    void delete(String id);

    void checkApp(BaseReq req);
}
