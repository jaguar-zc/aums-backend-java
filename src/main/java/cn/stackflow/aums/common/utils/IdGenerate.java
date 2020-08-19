package cn.stackflow.aums.common.utils;

import java.util.UUID;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-07-18 12:07
 */
public interface IdGenerate {
    IdGenerate DEFAULT = new IdGenerate(){};


    default String id() {
        return UUID.randomUUID().toString();
    }


}
