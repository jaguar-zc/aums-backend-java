package cn.stackflow.aums.domain.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-06-29 09:58
 */
@Getter
@Setter
@MappedSuperclass
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = -1369769400493496786L;

    @Id
    @Column(name = "ID",length = 40)
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @GeneratedValue(generator = "system-uuid")
    private String id;//ID

    //子表如果需要自己拷贝下去
//    @Column
//    private LocalDateTime createTime;//创建时间
//
//    @Column
//    private String createUser;//创建人
//
//    @Column
//    private LocalDateTime updateTime;//修改时间
//
//    @Column
//    private String updateUser;//修改人
//
//    @Column
//    private Integer deleted;//逻辑删除:0=未删除,1=已删除
}
