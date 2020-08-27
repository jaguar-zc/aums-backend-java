package cn.stackflow.aums.domain.service;

import cn.stackflow.aums.common.bean.DeptDTO;
import cn.stackflow.aums.common.bean.DeptMemberDTO;
import cn.stackflow.aums.common.bean.PageResult;
import cn.stackflow.aums.domain.entity.User;

import java.util.List;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-07-06 17:49
 */
public interface DeptService {

    PageResult<DeptDTO> list(PageResult page);

    void create(User user, DeptDTO deptDTO);

    void update(User user, DeptDTO deptDTO);

    void delete(User user, String id);
    List<DeptMemberDTO> listMember(String deptId);
}
