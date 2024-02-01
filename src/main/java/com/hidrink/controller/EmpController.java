package com.hidrink.controller;


import com.hidrink.annotaion.Recorder;
import com.hidrink.pojo.Emp;
import com.hidrink.pojo.EmpPageInfo;
import com.hidrink.pojo.Result;
import com.hidrink.service.EmpService;
import com.hidrink.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
public class EmpController {
    @Autowired
    public EmpService empService;

    @GetMapping("/emp") /*包含筛选*/
    public Result getEmp(@RequestParam(name = "pageNow", defaultValue = "1") Integer pageNum,
                         @RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize,
                         @RequestParam(name = "name", defaultValue = "")String name,
                         String startDate,
                         String endDate){
        log.info("前端管理员请求查看所有员工列表");
        if(!startDate.isEmpty() && !endDate.isEmpty()){
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            if(start.isAfter(end)){
                return Result.error("请输入有效日期范围！");
            }
        }

        EmpPageInfo empPageInfo = new EmpPageInfo(pageNum,pageSize,null,name,startDate,endDate,null);
        log.info("前端请求所有员工信息，第{}页，共{}条，条件为：{},{},{}",
        empPageInfo.getPageNow(), empPageInfo.getPageSize(), empPageInfo.getName(),
        empPageInfo.getStartDate(), empPageInfo.getEndDate());
        empPageInfo = empService.getEmp(empPageInfo);
        log.info("查询到符合条件的条目共{}条，现返回第{}页", empPageInfo.getTotalSize(), empPageInfo.getPageNow());
        return Result.success(empPageInfo);
    }

    @Recorder
    @PutMapping("/updateEmp")
    public Result updateEmp(@RequestBody Emp emp){
        log.info("前端要求修改员工{}的信息为{}",emp.getUsername(),emp);
        return Result.success(empService.updateEmp(emp));
    }

    @Recorder
    @PutMapping("/addEmp")
    public Result addEmp(@RequestBody Emp emp){
        if(emp.getDeptId() == null || emp.getUsername() == null ||
           emp.getPassword() == null || emp.getEntryTime() == null || emp.getGender() == null){
            return Result.error("缺少必填信息");
        }
        log.info("前端要求添加员工{}",emp.getUsername());
        return Result.success(empService.addEmp(emp));
    }

    @Recorder
    @DeleteMapping("/deleteEmp/{ids}")
    public Result deleteEmps(@PathVariable List<Integer> ids){
        log.info("前端要求删除id为{}的员工",ids);
        empService.deleteEmps(ids);
        return Result.success();
    }

    @PostMapping("/useremp")
    public Result getInfo(HttpServletRequest request){
        String jwt = request.getHeader("token");
        Integer id = (Integer) JwtUtils.parseJwt(jwt).get("id");
        log.info("前端员工：{}查看个人信息",id);
        return Result.success(empService.getInfo(id));
    }


}
