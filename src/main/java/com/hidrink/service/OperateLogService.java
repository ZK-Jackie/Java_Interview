package com.hidrink.service;

import com.hidrink.pojo.LogPageInfo;
import com.hidrink.pojo.OperateLog;

public interface OperateLogService {
    LogPageInfo getLog(LogPageInfo logPageInfo);

    void insert(OperateLog operateLog);
}
