package cn.stackflow.aums.domain.service.impl;
import cn.stackflow.aums.common.utils.StringUtils;
import cn.stackflow.aums.domain.entity.Resource.ResourceType;
import cn.stackflow.aums.web.app.pojo.BaseReq;
import com.google.common.collect.Sets;

import cn.stackflow.aums.common.bean.PageResult;
import cn.stackflow.aums.domain.entity.App;
import cn.stackflow.aums.domain.entity.Resource;
import cn.stackflow.aums.domain.repository.AppRepository;
import cn.stackflow.aums.domain.repository.ResourceRepository;
import cn.stackflow.aums.domain.service.AppService;
import io.jsonwebtoken.lang.Assert;
import org.checkerframework.checker.units.qual.A;
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

    @Autowired
    ResourceRepository resourceRepository;

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

        Resource resource = new Resource();
        resource.setAppId(app.getAppId());
        resource.setName(app.getName());
        resource.setType(ResourceType.APP);
        resource.setResourceLevel(0);
        resource.setSort(1);
        resource.setEnable(1);
        resource.setRoles(Sets.newHashSet());
        resourceRepository.save(resource);
    }

    @Override
    public void update(App app) {
        Optional<App> appOptional = appRepository.findById(app.getAppId());
        if(!appOptional.isPresent()){
            return;
        }
        App app1 = appOptional.get();
        app1.setName(app.getName());
        appRepository.save(app1);

        Optional<Resource> resourceOptional = resourceRepository.findByTypeAndAppId(ResourceType.APP,app.getAppId());
        if(!resourceOptional.isPresent()){
            return;
        }

        Resource resource = resourceOptional.get();
        resource.setName(app.getName());
        resourceRepository.save(resource);

    }

    @Override
    public void delete(String id) {
        appRepository.deleteById(id);
        resourceRepository.deleteByAppId(id);
    }

    @Override
    public void checkApp(BaseReq req) {
        Assert.hasLength(req.getAppId(),"AppId不能为空");
        Assert.hasLength(req.getSecret(),"Secret不能为空");
        Optional<App> appOptional = appRepository.findById(req.getAppId());
        Assert.isTrue(appOptional.isPresent(),"AppID错误");
        App app = appOptional.get();
        Assert.isTrue(StringUtils.equals(app.getSecret(),req.getSecret()),"Secret错误");
    }
}
