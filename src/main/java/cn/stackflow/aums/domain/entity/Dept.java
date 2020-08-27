package cn.stackflow.aums.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-07-01 18:18
 */
@Getter
@Setter
@Table(name = "t_sys_dept")
@Entity
public class Dept extends BaseEntity {

    private static final long serialVersionUID = 3375021962848745018L;


    @Column
    private String appId;//应用Id

    @Column
    private String name;//部门名称

    @Column
    private String remark;//部门描述

    @Column
    private LocalDateTime createTime;//创建时间

    @Column
    private String createUser;//创建人
}
