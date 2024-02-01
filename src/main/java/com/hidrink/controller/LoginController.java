package com.hidrink.controller;

import com.hidrink.annotaion.Recorder;
import com.hidrink.pojo.Login;
import com.hidrink.pojo.Register;
import com.hidrink.pojo.Result;
import com.hidrink.service.LoginService;
import com.hidrink.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Recorder
    @PostMapping("/login")
    public Result login(@RequestBody Login loginData){
        log.info("接收到请求，登录数据为{}",loginData);
        loginData = loginService.login(loginData);
        if(loginData.getIsSuccess()){
            Map<String,Object> claims = new HashMap<>();
            claims.put("id",loginData.getId());
            claims.put("role",loginData.getRole());
            claims.put("username",loginData.getUsername());

            String jwt = JwtUtils.generateJwt(claims);
            log.info("用户{}成功登陆，JWT令牌{}已发放。",loginData.getUsername(),jwt);
            return Result.success(jwt);    /*data区提供JWT，JWT中提供角色和角色名*/
        }else{
            return Result.error("用户名或密码错误");
        }
    }

    @PostMapping("/register")
    public Result register(@RequestBody Register registerData){
        log.info("前端注册用户，数据为{}",registerData);
        loginService.register(registerData);
        return Result.success();
    }

    @PostMapping("/checkState")
    public Result checkState(@RequestBody Login loginInfo){
        String token = loginInfo.getToken();
        log.info("前端校验token: {}",token);
        if(Objects.equals(token, "")){  //若令牌为空
            return Result.error("用户未登录");
        }else {
            try{
                Claims claims = JwtUtils.parseJwt(token);
                loginInfo.setId((Integer) claims.get("id"));
                Integer role = (Integer) claims.get("role");
                loginInfo.setRole(role.shortValue());
                loginInfo.setUsername((String) claims.get("username"));
                loginInfo.setIsSuccess(true);
            }catch (Exception ex){
                return Result.error("登录异常，请重新登录");
            }
            return Result.success(loginInfo);
        }
    }
}
