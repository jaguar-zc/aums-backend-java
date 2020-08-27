package cn.stackflow.aums.domain.service.impl;

import cn.stackflow.aums.common.bean.PageResult;
import cn.stackflow.aums.domain.entity.App;
import cn.stackflow.aums.domain.repository.AppRepository;
import cn.stackflow.aums.domain.service.AppService;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-08-27 09:20
 */
@Service
public class AppServiceImpl implements AppService {


    @Autowired
    AppRepository appRepository;

    @Override
    public PageResult<App> list(PageResult page) {
        PageRequest of = PageRequest.of(page.getPage() - 1, page.getSize());
        Page<App> deptPage = appRepository.findAll(of);
        page.setTotal(deptPage.getTotalElements());
        page.setRows(deptPage.getContent());
        return page;
    }

    @Override
    public void create(App app) {
        Assert.hasLength(app.getAppId(),"AppId不能为空");
        Assert.hasLength(app.getName(),"Name不能为空");
        Assert.isTrue(!appRepository.existsById(app.getAppId()),"AppId["+app.getAppId()+"]已存在");
        appRepository.save(app);
    }

    @Override
    public void update(App app) {
        Optional<App> appOptional = appRepository.findById(app.getAppId());
        if(appOptional.isPresent()){
            App app1 = appOptional.get();
            app1.setName(app.getName());
            appRepository.save(app1);
        }
    }

    @Override
    public void delete(String id) {
        appRepository.deleteById(id);
    }
}
