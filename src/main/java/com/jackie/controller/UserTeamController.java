package com.jackie.controller;

import com.jackie.pojo.Result;
import com.jackie.service.UserTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserTeamController {
    @Autowired
    private UserTeamService userTeamService;

    @GetMapping("/getTeamMember/{teamId}")
    public Result getTeamMember(@PathVariable Integer teamId){
        return Result.success(userTeamService.getTeamMembers(teamId));
    }

    @PutMapping("/addTeamMember")
     public Result addTeamMember(@RequestBody Integer teamId, @RequestBody Integer userId) {
        return Result.success(userTeamService.addTeamMember(userId, teamId));
    }

}
