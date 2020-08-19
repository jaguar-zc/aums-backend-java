package cn.stackflow.aums.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

/**
 * 资源表（resource）
 *
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-06-28 16:11
 */
@Table(name = "t_sys_resource")
@Entity
@Getter
@Setter
@AllArgsConstructor
public class Resource extends BaseEntity {

    public final static String RESPONSE_URI_SEGMENT = ",";
    private static final long serialVersionUID = -4299116402833379115L;

    @Column
    private String parentId;//所属父级权限ID  0,1,2

    @Column
    private String code;//权限CODE唯一代码

    @Column
    private String name;//资源名称

    @Column
    private String remark;//资源描述

    @Column
    @Enumerated(value = EnumType.STRING)
    private ResourceType type;//资源类型： 分组、模块、操作

    @Column
    private String uri;//URI规则 多个用逗号分开

    @Column
    private String icon;//图标

    @Column
    private Integer resourceLevel;//深度 1,2,3

    @Column
    private Integer sort;//排序

    @Column
    private Integer enable;//1启用;0停用

    @ManyToMany(cascade = CascadeType.REFRESH, mappedBy = "resources")
    private Set<Role> roles;

    public Resource() {
    }


    //资源类型：应用、分组、模块、操作
    public enum ResourceType {
        APP, GROUP, MODULE, BUTTON
    }


}
