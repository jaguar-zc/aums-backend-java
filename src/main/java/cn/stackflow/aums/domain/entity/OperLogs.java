
package cn.stackflow.aums.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 日志
 *
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-08-25 10:17
 */
@Getter
@Setter
@Table(name = "t_oper_logs")
@Entity
public class OperLogs extends BaseEntity {

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column
    private LocalDateTime createTime;//创建时间
    @Column
    private String operModule;
    @Column
    private Integer logsType;
    @Column
    private String operType;
    @Column
    private String operDesc;
    @Column
    private String operReqParam;
    @Column
    private String operRespParam;
    @Column
    private String throwable;//异常信息
    @Column
    private String operUserId;//创建人
    @Column
    private String operUserName;//创建人
    @Column
    private String operMethod;//创建人
    @Column
    private String operUri;//创建人
    @Column
    private String operIp;//创建人


}
