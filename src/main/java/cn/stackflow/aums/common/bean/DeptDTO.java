package cn.stackflow.aums.common.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-07-05 10:33
 */
@Getter
@Setter
public class DeptDTO {

    private String id;
    @NotEmpty
    private String name;//账号
    private String appId;//应用ID
    private String remark;//部门描述
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;//创建时间
    private String createUser;//创建人
}
