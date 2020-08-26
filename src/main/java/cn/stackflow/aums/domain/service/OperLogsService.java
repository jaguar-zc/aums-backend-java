package cn.stackflow.aums.domain.service;

import cn.stackflow.aums.common.bean.PageResult;
import cn.stackflow.aums.domain.entity.OperLogs;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-06-28 16:55
 */
public interface OperLogsService {
     PageResult<OperLogs> findList(PageResult pageResult, Integer logsType, String url);
     void insert(OperLogs operLogs);
}
