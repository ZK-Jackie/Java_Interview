package com.jackie.controller;

import com.jackie.pojo.Result;
import com.jackie.pojo.TeamPageInfo;
import com.jackie.service.TeamService;
import com.jackie.utils.JwtUtils;
import com.jackie.ws.ChatEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
public class TeamController {
    @Autowired
    private TeamService teamService;
    @Autowired
    private ChatEndpoint chatEndpoint;

    @GetMapping("/getAllTeam")
    public Result getAllTeam(@RequestParam(name = "username", defaultValue = "") String name,
                          @RequestParam(name = "pageNow", defaultValue = "1") Integer pageNow,
                          HttpServletRequest request){
        Integer userNowId = (Integer) JwtUtils.parseJwt(request.getHeader("token")).get("id");
        TeamPageInfo teamPageInfo = new TeamPageInfo(userNowId, name, pageNow, null, null);
        log.info("前端{}请求获取团队信息", userNowId);
        return Result.success(teamService.getTeam(teamPageInfo));
    }

    @GetMapping("/newTeam/{ids}")
    public Result newTeam(@PathVariable List<Integer> ids, HttpServletRequest request){
        Integer userNowId = (Integer) JwtUtils.parseJwt(request.getHeader("token")).get("id");
        log.info("前端{}请求获取与{}创建团队", userNowId, ids);
        return Result.success(teamService.newTeam(userNowId, ids));
    }

    @GetMapping("/getPrivateTeam")
    public Result getPrivateTeam(@RequestParam(name = "username", defaultValue = "") String name,
                                 @RequestParam(name = "pageNow", defaultValue = "1") Integer pageNow,
                                 HttpServletRequest request){
        Integer userNowId = (Integer) JwtUtils.parseJwt(request.getHeader("token")).get("id");
        TeamPageInfo teamPageInfo = new TeamPageInfo(userNowId, name, pageNow, null, null);
        log.info("前端{}请求获取私聊团队ID", userNowId);
        return Result.success(teamService.getPrivateTeam(teamPageInfo));
    }

    @PutMapping("/addMemberByTeam/{nowUserId}/{teamId}/{teamIds}")
    public Result addMember(@PathVariable Integer nowUserId,@PathVariable Integer teamId, @PathVariable List<Integer> teamIds) {
        teamService.addMemberByTeamIds(nowUserId , teamId, teamIds);
        return Result.success();
    }

    @PutMapping("/newTeamByTeam/{creatorId}/{teamIds}")
    public Result newTeamByTeam(@PathVariable Integer creatorId, @PathVariable List<Integer> teamIds){
        return Result.success(teamService.newTeamByTeamIds(creatorId,teamIds));
    }

    @GetMapping("/getSubTeamId/{nowUserId}/{teamId}")
    public Result getSubTeamId(@PathVariable Integer nowUserId, @PathVariable Integer teamId){
        return Result.success(teamService.getSubTeamId(nowUserId,teamId));
    }


}
