package cn.stackflow.aums.domain.service;

import cn.stackflow.aums.common.bean.ResourceDTO;
import cn.stackflow.aums.common.utils.JSONUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@EnableTransactionManagement
public class ResourceServiceTest {


    @Autowired
    ResourceService resourceService;
    @Test
    public void getResourceListByUserId() {

        List<ResourceDTO> resourceListByUserId = resourceService.getResourceListByUserId("5f168fc7-7da0-49a8-a291-75dea0921656");

        System.out.println(JSONUtils.object2Json(resourceListByUserId));

    }
}