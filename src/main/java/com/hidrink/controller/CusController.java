package com.hidrink.controller;

import com.hidrink.annotaion.Recorder;
import com.hidrink.pojo.*;
import com.hidrink.service.CusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


//顾客
@Slf4j
@RestController
public class CusController {
    @Autowired
    public CusService cusService;

    @GetMapping("/daily")
    public Result getDaily(){
        log.info("前端管理员请求获取数据统计");
        return Result.success(cusService.getDaily());
    }


    //查询顾客页面
    @GetMapping("/list")
    public Result getCusPage(@RequestParam Cus cus,
                             @RequestParam Integer pageNow,
                             @RequestParam Integer pageSize,
                             @RequestParam String name,
                             @RequestParam String startDate,
                             @RequestParam String endDate){
        log.info("前端请求查看顾客所有列表");
        if(!startDate.isEmpty() && !endDate.isEmpty()){
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            if(start.isAfter(end)){
                return Result.error("请输入有效日期范围！");
            }
        }

        CusPageInfo cusPageInfo = new CusPageInfo(pageNow,pageSize,null,name,startDate,endDate,null);
        log.info("前端请求所有顾客信息，第{}页，共{}条，条件为：{},{},{}",
                cusPageInfo.getPageNow(), cusPageInfo.getPageSize(), cusPageInfo.getName(),
                cusPageInfo.getStartDate(), cusPageInfo.getEndDate());
        cusPageInfo = cusService.getCus(cusPageInfo);
        log.info("查询到符合条件的条目共{}条，现返回第{}页", cusPageInfo.getTotalSize(), cusPageInfo.getPageNow());
        return Result.success(cusPageInfo);
    }

    //更改顾客信息
    @Recorder
    @PutMapping("/updateCus")
    public Result updateCus(@RequestBody Cus cus){
        log.info("前端要求修改顾客{}的信息为{}",cus.getId(),cus);
        return Result.success(cusService.updateCus(cus));
    }
    //添加顾客信息
    @Recorder
    @PutMapping("/addCus")
    public Result addCus(@RequestBody Cus cus){
        if(cus.getId() == null || cus.getUsername() == null ||
                cus.getPassword() == null || cus.getCreateTime() == null){
            return Result.error("缺少必填信息");
        }
        log.info("前端要求添加顾客{}",cus.getId());
        return Result.success(cusService.addCus(cus));
    }

    //删除个人顾客信息
    @Recorder
    @DeleteMapping("/deleteCus/{ids}")
    public Result deleteCus(@PathVariable List<Integer> ids){
        log.info("前端请求删除顾客号为{}的商品",ids);
        cusService.deleteCus(ids);
        return Result.success();
    }

}
