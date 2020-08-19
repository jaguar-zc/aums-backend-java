package cn.stackflow.aums.domain.service.impl;

import cn.stackflow.aums.common.bean.*;
import cn.stackflow.aums.common.cache.UserCache;
import cn.stackflow.aums.common.exception.ServiceException;
import cn.stackflow.aums.common.password.PasswordEncoder;
import cn.stackflow.aums.common.utils.RegexUtils;
import cn.stackflow.aums.common.utils.StringUtils;
import cn.stackflow.aums.domain.entity.Dept;
import cn.stackflow.aums.domain.entity.Role;
import cn.stackflow.aums.domain.entity.User;
import cn.stackflow.aums.domain.repository.DeptRepository;
import cn.stackflow.aums.domain.repository.RoleRepository;
import cn.stackflow.aums.domain.repository.UserRepository;
import cn.stackflow.aums.domain.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {


    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    DeptRepository deptRepository;

    @Autowired
    UserCache userCache;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public PageResult<UserDTO> list(PageResult page) {
        PageRequest of = PageRequest.of(page.getPage() - 1, page.getSize());
        Specification<User> specification = new Specification<User>() {
            @Override
            public javax.persistence.criteria.Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
//                if (StringUtils.isNotEmpty(deptId)) {
//                    predicates.add(criteriaBuilder.equal(root.get("deptId"), deptId));
//                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        Page<User> deptPage = userRepository.findAll(specification, of);
        page.setTotal(deptPage.getTotalElements());
        page.setRows(deptPage.getContent().stream().map(item -> this.getUser(item.getId())).collect(Collectors.toList()));
        return page;
    }

    @Override
    public List<IdNameDTO> listSimpleUserByDeptId(String deptId) {
        List<User> userList = userRepository.findByDeptId(deptId);
        return userList.stream().map(item -> {
            IdNameDTO idNameDTO = new IdNameDTO();
            idNameDTO.setId(item.getId());
            idNameDTO.setName(item.getName());
            return idNameDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public User get(String userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public UserDTO getUser(String id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new ServiceException("用户不存在");
        }
        User user = userOptional.get();

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setName(user.getName());
        userDTO.setPhone(user.getPhone());
        userDTO.setIcon(user.getIcon());
        userDTO.setEnable(user.getEnable());
        userDTO.setDeptId(user.getDept().getId());
        userDTO.setDeptName(user.getDept().getName());
        List<Role> roleList = roleRepository.findByIdIn(user.getRoleIds());
        userDTO.setRoleList(roleList.stream().map(item -> {
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setId(item.getId());
            roleDTO.setCode(item.getCode());
            roleDTO.setName(item.getName());
            return roleDTO;
        }).collect(Collectors.toList()));

        Dept dept = user.getDept();
        if (dept != null) {
            userDTO.setDeptId(dept.getId());
            userDTO.setDeptName(dept.getName());
        }


        return userDTO;
    }

    @Override
    public Optional<User> getUserInfoByUsername(String username) {
        User user = userCache.get(username);
        return Optional.ofNullable(user);
//        return userRepository.findByUsername(username);
    }


    @Override
    public UserDTO create(UserDTO userDTO) {
        log.info("createUser:{}", userDTO.getUsername());

        Assert.hasText(userDTO.getUsername(), "缺少参数[username]");
        Assert.hasText(userDTO.getName(), "缺少参数[name]");
        Assert.hasText(userDTO.getPassword(), "缺少参数[password]");
        Assert.hasText(userDTO.getDeptId(), "缺少参数[deptId]");
        Assert.hasText(userDTO.getPhone(), "缺少参数[phone]");
        Assert.isTrue(RegexUtils.isMobileSimple(userDTO.getPhone()), "手机号错误");
        Assert.isTrue(RegexUtils.isUsername(userDTO.getUsername()), "用户名格式不正确");
        //省略验证
        Assert.isTrue(userRepository.countByUsername(userDTO.getUsername()) == 0, "用户名已存在");
//        Assert.isTrue(userRepository.countByPhone(userDTO.getPhone()) == 0,"手机号已存在");
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setName(userDTO.getName());
        user.setPhone(userDTO.getPhone());
        user.setDept(deptRepository.getOne(userDTO.getDeptId()));
        List<String> roleIds = userDTO.getRoleList().stream().map(item -> item.getId() + "").collect(Collectors.toList());
        user.setRoles(String.join(User.USER_ROLE_SEGMENT, roleIds));
        user.setSalt(UUID.randomUUID().toString().substring(0, 8));
        String encodePassword = passwordEncoder.encode(user.getSalt(), user.getPassword());
        user.setPassword(encodePassword);
        user.setEnable(1);
//        try {
//            String objectName = IdGenerate.DEFAULT.id() + ".jpg";
//            String icon = minioOssClinet.putObject(objectName, CreateNamePicture.generateImg(userDTO.getName()), MinioContentType.IMAGE_JPG.getContentType());
//            user.setIcon(icon);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        userRepository.save(user);
        return this.getUser(user.getId());
    }


    @Override
    public void updatePhone(UpdatePhoneDTO updatePhoneDTO) {
        log.info("updatePhone:{},{}", updatePhoneDTO.getUserId(), updatePhoneDTO.getPhone());

        Assert.hasText(updatePhoneDTO.getUserId(), "缺少参数[userId]");
        Assert.hasText(updatePhoneDTO.getPhone(), "请输入手机号");
        Assert.isTrue(RegexUtils.isMobileSimple(updatePhoneDTO.getPhone()), "手机号错误");

        Optional<User> userOptional = userRepository.findById(updatePhoneDTO.getUserId());
        if (!userOptional.isPresent()) {
            throw new ServiceException("用户不存在");
        }
        User user = userOptional.get();
        log.info("updatePhone:{},  {} to {}", user.getUsername(), user.getPhone(), updatePhoneDTO.getPhone());
        this.checkUserEnable(user);
        user.setPhone(updatePhoneDTO.getPhone());
        userRepository.save(user);
        userCache.refresh(user.getUsername());
    }

    @Override
    public void updatePwd(UpdatePwdDTO updatePwdDTO) {
        log.info("updatePwd:{},{},{}", updatePwdDTO.getUserId(), updatePwdDTO.getOldPassword(), updatePwdDTO.getNewPassword());

        Assert.hasText(updatePwdDTO.getUserId(), "缺少参数[userId]");
        Assert.hasText(updatePwdDTO.getOldPassword(), "请输入旧密码");
        Assert.hasText(updatePwdDTO.getNewPassword(), "请输入新密码");

        Optional<User> userOptional = userRepository.findById(updatePwdDTO.getUserId());
        if (!userOptional.isPresent()) {
            throw new ServiceException("用户不存在");
        }

        User user = userOptional.get();
        this.checkUserEnable(user);


        String encodeOldPassword = passwordEncoder.encode(user.getSalt(), updatePwdDTO.getOldPassword());
        if (!StringUtils.equals(encodeOldPassword, user.getPassword())) {
            throw new ServiceException("旧密码错误");
        }
        String encodeNewPassword = passwordEncoder.encode(user.getSalt(), updatePwdDTO.getNewPassword());

        log.info("user:{},updatePwd:{} ,to: {}", user.getUsername(), user.getPassword(), encodeNewPassword);

        user.setPassword(encodeNewPassword);
        userRepository.save(user);
    }


    private void checkUserEnable(User user){
        Assert.isTrue(user.getEnable() == 1,"该用户已停用");
    }

    @Override
    public void close(String userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new ServiceException("用户不存在");
        }
        User user = userOptional.get();
        log.info("close:{}", user.getUsername());
        user.setEnable(0);
        userRepository.save(user);
    }
}
