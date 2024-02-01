package com.jackie.service.impl;

import com.jackie.mapper.TeamMapper;
import com.jackie.mapper.UserMapper;
import com.jackie.mapper.UserTeamMapper;
import com.jackie.service.UserService;
import com.jackie.service.UserTeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserTeamServiceImpl implements UserTeamService {
    @Autowired
    private UserTeamMapper userTeamMapper;
    @Autowired
    private TeamMapper teamMapper;

    @Override
    public List<Integer> getTeamMembers(Integer teamId) {
        return userTeamMapper.selectMemberIdByTeamId(teamId);
    }

    @Override
    public Boolean addTeamMember(Integer userId, Integer teamId) {
        //1.team人数增加
        teamMapper.updateTeamJoinNum(teamId);
        //2.添加user_team记录
        return userTeamMapper.insertUserTeam(userId, teamId);
    }
}
