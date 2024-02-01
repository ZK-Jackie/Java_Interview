package com.jackie.service;

import com.jackie.pojo.TeamPageInfo;

import java.util.List;

public interface TeamService {

    TeamPageInfo getTeam(TeamPageInfo teamPageInfo);

    Boolean newTeam(Integer userNowId, List<Integer> ids);

    TeamPageInfo getPrivateTeam(TeamPageInfo teamPageInfo);

    Boolean addMember(List<Integer> userIds, Integer teamId);

    Boolean newTeamByTeamIds(Integer creatorId, List<Integer> teamIds);

    List<Integer> getSubTeamId(Integer nowUserId, Integer teamId);

    Boolean addMemberByTeamIds(Integer nowUserId, Integer teamId, List<Integer> teamIds) ;
}
