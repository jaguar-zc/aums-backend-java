package cn.stackflow.aums.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-08-27 09:18
 */
@Getter
@Setter
@Table(name = "t_sys_app")
@Entity
public class App {
    @Id
    private String appId;
    @Column
    private String name;
    @Column
    private String secret;
}
