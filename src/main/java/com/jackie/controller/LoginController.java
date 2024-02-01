package com.jackie.controller;

import com.jackie.pojo.Login;
import com.jackie.pojo.Register;
import com.jackie.pojo.Result;
import com.jackie.pojo.User;
import com.jackie.service.LoginService;
import com.jackie.utils.JwtUtils;
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

    @PostMapping("/login")
    public Result login(@RequestBody Login loginData){
        log.info("接收到请求，登录数据为{}",loginData);
        User user = loginService.login(loginData);
        if(loginData.getIsSuccess()){
            Map<String,Object> claims = new HashMap<>();
            claims.put("id",user.getUserId());
            claims.put("username",user.getUsername());

            String jwt = JwtUtils.generateJwt(claims);
            log.info("用户{}成功登陆，JWT令牌{}已发放。", loginData.getUsername(), jwt);
            return Result.success(jwt);    /*data区提供JWT，JWT中提供user_id和用户名*/
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
                loginInfo.setUserId((Integer) claims.get("id"));
                loginInfo.setUsername((String) claims.get("username"));
            }catch (Exception ex){
                return Result.error("登录异常，请重新登录");
            }
            loginInfo.setUrl(loginService.getAvatarUrl(loginInfo.getUserId()));
            return Result.success(loginInfo);
        }
    }
}
