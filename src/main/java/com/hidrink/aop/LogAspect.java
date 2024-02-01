package com.hidrink.aop;

import com.alibaba.fastjson.JSONObject;
import com.hidrink.pojo.OperateLog;
import com.hidrink.service.OperateLogService;
import com.hidrink.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;

@Component
@Slf4j
@Aspect
public class LogAspect {
    @Autowired  //每一次的网页请求自然会在IOC中
    private HttpServletRequest request;

    @Autowired
    private OperateLogService operateLogService;

    @Around("@annotation(com.hidrink.annotaion.Recorder)")
    public Object recordLog(ProceedingJoinPoint joinPoint) throws Throwable {
        //要记录的内容包括操作人id，操作时间，操作类名，操作方法名，操作方法函数，方法返回值金额操作耗时
        //日志文件数据来源主要从jwt中获得，jwt在每次网页请求的request里面，且request肯定已经在IOC中
        /* 网页 → aop → controller → service → mapper → service → controller → aop → 网页
         * 1.从request中的jwt入手，拿到用户基本信息（注意额外避开登录操作，新登录用户无token）
         * 2.后面全部都从jointPoint入手，从中调取用户意图
         * */
        //操作时间
        LocalDateTime operateTime = LocalDateTime.now();
        //操作目标类
        String className = joinPoint.getTarget().getClass().getName();
        //操作目标方法
        String methodName = joinPoint.getSignature().getName();
        //操作目标参数
        Object[] args = joinPoint.getArgs();
        String methodParams = Arrays.toString(args);
        //操作后的返回值，result存放controller的返回值
        Long startTime = System.currentTimeMillis();    //开始计时
        Object result = joinPoint.proceed(); //返回服务器用
        String resultJSON = JSONObject.toJSONString(result);   //数据库记录用
        //记录操作人信息
        String jwt = request.getHeader("token");
        if(request.getRequestURL().toString().contains("login")){   //登录情况，从controller中获取登录人信息
            Map<String,Object> map = JSONObject.parseObject(resultJSON);    //可直接：JSON->Map
            if ((Integer) map.get("code") == 0) {   //已经发生了异常，可直接返回，无需校验jwt
                return result;
            }
            jwt = (String) map.get("data");
        }
        Claims claims = JwtUtils.parseJwt(jwt);
        Integer operatorUser = (Integer) claims.get("id");
        Short operatorRole = ((Integer)claims.get("role")).shortValue();

        Long endTime = System.currentTimeMillis();    //结束计时
        //计算操作总耗时
        Long constTime = endTime - startTime;

        //打包log包并插入
        OperateLog operateLog = new OperateLog(null,operatorUser,operatorRole,operateTime,className,methodName,methodParams,resultJSON,constTime);
        operateLogService.insert(operateLog);

        log.info("AOP记录日志：{}",operateLog);
        return result;
    }
}
