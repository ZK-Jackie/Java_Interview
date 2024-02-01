package com.jackie.controller;

import com.jackie.pojo.Result;
import com.jackie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/searchUser")
    public Result searchUser(@RequestParam String username){
        return Result.success(userService.searchUser(username));
    }
}
