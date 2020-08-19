package cn.stackflow.aums.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 角色表（role）
 *
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-06-28 16:11
 */
@Entity
@Table(name = "t_sys_role")
@Getter
@Setter
public class Role extends BaseEntity {

    private static final long serialVersionUID = 7778137776336335417L;


    @Column
    private String name;//角色名称

    @Column
    private String code;//角色编码

    @Column
    private String remark;//角色名称

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "t_sys_role_resource", joinColumns = {@JoinColumn(name = "role_id")}, inverseJoinColumns = {@JoinColumn(name = "resource_id")})
    private Set<Resource> resources = new HashSet<Resource>();

}