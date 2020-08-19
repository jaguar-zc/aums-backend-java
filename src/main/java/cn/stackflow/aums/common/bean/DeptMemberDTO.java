package cn.stackflow.aums.common.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 *
 * 部门成员
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-07-11 15:56
 */
@Setter
@Getter
public class DeptMemberDTO {
    private String id;
    private String name;
    private List<IdNameDTO> members;
}
