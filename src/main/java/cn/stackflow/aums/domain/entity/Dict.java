package cn.stackflow.aums.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 字典
 *
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-07-08 09:31
 */
@Getter
@Setter
@Table(name = "t_sys_dict")
@Entity
public class Dict {

    private static final long serialVersionUID = -6712267532276820060L;
    @Id
    private String dataCode;
    private String dataValue;
    private String dataDesc;

}
