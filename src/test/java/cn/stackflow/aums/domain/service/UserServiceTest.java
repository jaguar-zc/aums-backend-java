package cn.stackflow.aums.domain.service;

import cn.stackflow.aums.common.bean.UserDTO;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;


    @Test
    public void create() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("admin");
        userDTO.setPassword("123456");
        userDTO.setName("管理员");
        userDTO.setPhone("18981063280");
        userDTO.setDeptId("1");
        userDTO.setEnable(1);
        userDTO.setRoleList(Arrays.asList());
        userService.create(userDTO);
    }
}