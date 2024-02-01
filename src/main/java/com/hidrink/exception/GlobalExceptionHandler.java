package com.hidrink.exception;

import com.hidrink.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice   //全局异常处理，RestControllerAdvice = ControllerAdvice + ResponseBody(JSON)
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)  //捕获所有类型的exception
    public Result ex(Exception e){
        String msg = String.valueOf(e.getCause());
        if(msg.contains("Duplicate entry")){
            if(msg.contains("dept")){
                return Result.error("部门已存在");
            } else if (msg.contains("emp")) {
                return Result.error("用户名已存在");
            }
        }else if(msg.contains("which invoked this stored function/trigger")){
            return Result.error("仍有员工任职于该部门，不可删除");
        }
        log.info("出现异常：{}",msg);    //输出异常信息
        return Result.error("服务器异常");
    }
}

