package com.hidrink.controller;

import com.hidrink.annotaion.Recorder;
import com.hidrink.pojo.LogPageInfo;
import com.hidrink.pojo.Result;
import com.hidrink.service.OperateLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class OperateLogController {
    @Autowired
    public OperateLogService operateLogService;

    @Recorder
    @GetMapping("/log")
    public Result getLog(@RequestParam(name = "pageNow", defaultValue = "1") Integer pageNum,
                         @RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize){
        log.info("前端管理员查看所有日志信息");
        LogPageInfo logPageInfo = new LogPageInfo(pageNum,pageSize,null,null);
        return Result.success(operateLogService.getLog(logPageInfo));
    }
}
