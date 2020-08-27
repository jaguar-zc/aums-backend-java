package cn.stackflow.aums.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
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
@Table(name = "t_sys_dict_value")
@Entity
public class DictValue extends BaseEntity {
    private static final long serialVersionUID = -6712267532276820060L;
    @Column
    private String dictTypeId;
    @Column
    private String dataCode;
    @Column
    private String dataValue;
}
