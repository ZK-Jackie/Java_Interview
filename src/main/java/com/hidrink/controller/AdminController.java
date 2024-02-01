package com.hidrink.controller;

import com.hidrink.pojo.Result;
import com.hidrink.service.AdminService;
import com.hidrink.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class AdminController {
    @Autowired
    private AdminService adminService ;

    @PostMapping("/useradmin")
    public Result getInfo(HttpServletRequest request){
        String jwt = request.getHeader("token");
        Integer id = (Integer) JwtUtils.parseJwt(jwt).get("id");
        log.info("前端管理员：{}获取个人信息",id);
        return Result.success(adminService.getInfo(id));
    }


}
