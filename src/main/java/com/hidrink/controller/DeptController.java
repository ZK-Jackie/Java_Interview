package com.hidrink.controller;

import com.hidrink.annotaion.Recorder;
import com.hidrink.pojo.Dept;
import com.hidrink.pojo.Result;
import com.hidrink.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
public class DeptController {
    @Autowired
    public DeptService deptService;

    @GetMapping("/dept")
    public Result getDept(){
        log.info("前端请求所有部门详细信息");
        return Result.success(deptService.getDept());
    }

    @GetMapping("/deptList")
    public Result getDeptList(){
        log.info("前端请求部门列表");
        Map<Integer,String> result = deptService.getDeptList();
        return Result.success(result);
    }

    @Recorder
    @PutMapping("/updateDept")
    public Result updateDept(@RequestBody Dept dept){
//        log.info("前端请求修改部门信息为：{}",dept);
        deptService.updateDeptByID(dept);
        return Result.success();
    }

    @Recorder
    @PutMapping("/addDept")
    public Result addDept(@RequestBody Dept dept){
        log.info("前端请求添加部门{}",dept.getName());
        deptService.addDept(dept);
        return Result.success();
    }

    @Recorder
    @DeleteMapping("/deleteDept/{id}")
    public Result deleteDept(@PathVariable Integer id){
        log.info("前端请求删除id为{}的部门",id);
        deptService.deleteDept(id);
        return Result.success();
    }
}
