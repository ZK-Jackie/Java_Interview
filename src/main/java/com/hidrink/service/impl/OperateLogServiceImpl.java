package com.hidrink.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hidrink.mapper.OperateLogMapper;
import com.hidrink.pojo.LogPageInfo;
import com.hidrink.pojo.OperateLog;
import com.hidrink.service.OperateLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperateLogServiceImpl implements OperateLogService {
    @Autowired
    public OperateLogMapper operateLogMapper;

    @Override
    public LogPageInfo getLog(LogPageInfo logPageInfo) {
        //1.设置分页参数
        PageHelper.startPage(logPageInfo.getPageNow(), logPageInfo.getPageSize());
        //2.将查询结果交给Page泛型
        List<OperateLog> data = operateLogMapper.select(logPageInfo);
        //3.包装数据
        logPageInfo.setTableData(((Page<OperateLog>) data).getResult());
        logPageInfo.setTotalSize(((Page<OperateLog>) data).getTotal());
        //4.返回controller
        return logPageInfo;
    }

    @Override
    public void insert(OperateLog operateLog) {
        if(operateLog.getReturnValue().length() >= 2000){
            operateLog.setReturnValue("Get All Logs");
        }
        operateLogMapper.insert(operateLog);
    }
}
