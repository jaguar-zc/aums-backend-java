package cn.stackflow.aums.domain.service;

import cn.stackflow.aums.common.bean.DictTypeDTO;
import cn.stackflow.aums.common.bean.DictValueDTO;
import cn.stackflow.aums.common.bean.PageResult;
import cn.stackflow.aums.domain.entity.User;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-07-08 09:57
 */
public interface DictService {

    PageResult<DictTypeDTO> getTypeList(PageResult page);

    PageResult<DictValueDTO> getValueList(PageResult page, String dictTypeId);

    void createDictType(User user, DictTypeDTO dictType);

    void createDictValue(User user, DictValueDTO dictValue);

    void updateDictType(DictTypeDTO dictType);

    void deleteDictType(String id);

    void deleteDictValue(String id);

}
