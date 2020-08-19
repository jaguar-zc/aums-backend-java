package cn.stackflow.aums.common.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-07-05 10:33
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DictPageDTO {
    private String code;//code
    private DictDTO dict;//字典
}
