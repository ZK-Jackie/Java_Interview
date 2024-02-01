package com.jackie.controller;


import com.jackie.pojo.Result;
import com.jackie.service.MessageService;
import com.jackie.utils.JwtUtils;
import com.jackie.ws.ChatEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class MessageController {
    @Autowired
    private MessageService messageService;

    @GetMapping("/getHistoryMessages/{teamId}")
    public Result getHistoryMessages(@PathVariable Integer teamId, HttpServletRequest request){
        //获取历史记录信息+作已读标记
        Integer userNowId = (Integer) JwtUtils.parseJwt(request.getHeader("token")).get("id");
        return Result.success(messageService.getHistoryMessages(userNowId,teamId));
    }

    @DeleteMapping("/withdrawMessage/{messageId}")
    public Result withdrawMessage(@PathVariable Integer messageId){
        return Result.success(messageService.withdrawMessage(messageId));
    }
}
