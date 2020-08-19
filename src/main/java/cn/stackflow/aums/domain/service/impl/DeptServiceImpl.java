package cn.stackflow.aums.domain.service.impl;

import cn.stackflow.aums.common.bean.DeptDTO;
import cn.stackflow.aums.common.bean.DeptMemberDTO;
import cn.stackflow.aums.common.bean.IdNameDTO;
import cn.stackflow.aums.common.bean.PageResult;
import cn.stackflow.aums.common.utils.StringUtils;
import cn.stackflow.aums.domain.entity.Dept;
import cn.stackflow.aums.domain.entity.User;
import cn.stackflow.aums.domain.repository.DeptRepository;
import cn.stackflow.aums.domain.service.DeptService;
import cn.stackflow.aums.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-07-06 17:49
 */
@Service
public class DeptServiceImpl implements DeptService {


    @Autowired
    DeptRepository deptRepository;

    @Autowired
    UserService userService;

    @Override
    public PageResult<DeptDTO> list(PageResult page) {
        PageRequest of = PageRequest.of(page.getPage() - 1, page.getSize(), Sort.by(Sort.Direction.DESC, "createTime"));
        Page<Dept> deptPage = deptRepository.findAll(of);
        page.setTotal(deptPage.getTotalElements());
        page.setRows(deptPage.getContent().stream().map(item -> {
            DeptDTO deptDTO = new DeptDTO();
            deptDTO.setRemark(item.getRemark());
            deptDTO.setCreateTime(item.getCreateTime());
            deptDTO.setCreateUser(item.getCreateUser());
            deptDTO.setId(item.getId());
            deptDTO.setName(item.getName());
            return deptDTO;
        }).collect(Collectors.toList()));
        return page;
    }


    @Override
    public void create(User user, DeptDTO deptDTO) {
        Dept dept = new Dept();
        dept.setName(deptDTO.getName());
        dept.setRemark(deptDTO.getRemark());
        dept.setCreateTime(LocalDateTime.now());
        dept.setCreateUser(user.getUsername());
        deptRepository.saveAndFlush(dept);
    }

    @Override
    public void update(User user, DeptDTO deptDTO) {
        Optional<Dept> deptOptional = deptRepository.findById(deptDTO.getId());
        Assert.isTrue(deptOptional.isPresent(), "部门ID[" + deptDTO.getId() + "]不存在");
        Dept dept = deptOptional.get();
        dept.setName(deptDTO.getName());
        dept.setRemark(deptDTO.getRemark());
        deptRepository.saveAndFlush(dept);
    }

    @Override
    public void remove(User user, String id) {
        deptRepository.deleteById(id);
    }

    @Override
    public List<DeptMemberDTO> listMember(String deptId) {
        List<Dept> all = new ArrayList<Dept>();
        if (StringUtils.isNotEmpty(deptId)) {
            Optional<Dept> optionalDept = deptRepository.findById(deptId);
            all.add(optionalDept.get());
        } else {
            all = deptRepository.findAll();
        }
        List<DeptMemberDTO> collect = all.stream().map(item -> {
            DeptMemberDTO deptMember = new DeptMemberDTO();
            deptMember.setId(item.getId());
            deptMember.setName(item.getName());
            List<IdNameDTO> idNameDTOS = userService.listSimpleUserByDeptId(item.getId());
            deptMember.setMembers(idNameDTOS);
            return deptMember;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public void delete(User user, String id) {
        deptRepository.deleteById(id);
    }
}
