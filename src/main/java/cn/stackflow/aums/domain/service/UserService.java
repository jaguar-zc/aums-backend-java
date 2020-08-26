package cn.stackflow.aums.domain.service;

import cn.stackflow.aums.common.bean.*;
import cn.stackflow.aums.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {


    PageResult<UserDTO> list(PageResult page);

    List<IdNameDTO> listSimpleUserByDeptId(String deptId);

    User get(String userId);

    UserDTO getUser(String id);

    Optional<User> getUserInfoByUsername(String username);

    UserDTO create(UserDTO userDTO);

    void updatePwd(UpdatePwdDTO updatePwdDTO);

    void updatePhone(UpdatePhoneDTO updatePhoneDTO);

    void update(UserDTO userDTO);

    void delete(User user, String id);
}
