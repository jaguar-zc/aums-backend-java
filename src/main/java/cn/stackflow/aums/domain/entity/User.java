package cn.stackflow.aums.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-07-01 18:18
 */
@Getter
@Setter
@Table(name = "t_sys_user")
@Entity
public class User extends BaseEntity {


    public final static String USER_ROLE_SEGMENT = ",";

    private static final long serialVersionUID = 7158444941356667514L;

    @Column
    private String appId;//应用Id

    @Column
    private String username;//账号
    @JsonIgnore
    @Column
    private String password;//密码
    @Column
    private String name;//姓名
    @Column
    private String phone;//手机号
    @JsonIgnore
    @Column
    private String salt;//盐
    @Column
    private String icon;//图标

    @OneToOne(targetEntity = Dept.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "dept_id", referencedColumnName = "id")
    private Dept dept;//部门

    @Column
    private String roles;//角色

    @Column
    private Integer enable;// 0:禁用 1:启用

    @Transient
    public List<String> getRoleIds(){
        String[] split = roles.split(USER_ROLE_SEGMENT);
        return Arrays.asList(split);
    }


}
