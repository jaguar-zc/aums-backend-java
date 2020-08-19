package cn.stackflow.aums.domain.service;

import cn.stackflow.aums.common.bean.DictDTO;
import cn.stackflow.aums.common.bean.PageResult;
import cn.stackflow.aums.domain.entity.User;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-07-08 09:57
 */
public interface DictService {

    PageResult<DictDTO> list(PageResult page);

    DictDTO get(String code);

    DictDTO get(String code, String defaultValue);

    void update(String code, String value);

    void delete(String code);

    void create(User user, DictDTO deptDTO);
}
