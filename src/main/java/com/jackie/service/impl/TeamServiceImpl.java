package com.jackie.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jackie.mapper.MessageMapper;
import com.jackie.mapper.TeamMapper;
import com.jackie.mapper.UserMapper;
import com.jackie.mapper.UserTeamMapper;
import com.jackie.pojo.Team;
import com.jackie.pojo.TeamPageInfo;
import com.jackie.service.TeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class TeamServiceImpl implements TeamService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TeamMapper teamMapper;
    @Autowired
    private UserTeamMapper userTeamMapper;
    @Autowired
    private MessageMapper messageMapper;


    @Override
    public TeamPageInfo getTeam(TeamPageInfo teamPageInfo) {
        //1.先获取用户所拥有的全部teamId
        List<Integer> teamIds = userTeamMapper.selectTeamIdById(teamPageInfo.getId());
        //2.条件查询并分页team信息
        paginate(teamIds, teamPageInfo, true);
        //3.控制台输出日志
        log.info("查询到符合条件的条目共{}条，现返回第{}页", teamPageInfo.getTotalSize(), teamPageInfo.getPageNow());
        return teamPageInfo;
    }

    @Override
    public Boolean newTeam(Integer userNowId, List<Integer> ids) {
        //0.让ids包括自己
        ids.add(userNowId);
        //1.创建team并获得新team_id
        Team team = new Team();
        String username = userMapper.selectUserById(userNowId).getUsername();
        team.setName(username + "的团队");
        team.setJoinNum(ids.size());
        team.setCreatorId(userNowId);
        teamMapper.newTeam(team);
        //1.1 获取新team_id
        Integer newTeamId = team.getTeamId();
        //2.填充user_team表，对方+自己
        ids.forEach((memberId) -> userTeamMapper.addUserTeam(newTeamId, memberId, LocalDateTime.now()));
        return true;
    }

    @Override
    public TeamPageInfo getPrivateTeam(TeamPageInfo teamPageInfo) {
        //1.先获取用户所拥有的全部私聊teamId
        List<Integer> teamIds = userTeamMapper.selectPrivateTeamIdById(teamPageInfo.getId());
        //2.查询并分页team信息
        paginate(teamIds, teamPageInfo, false);
        //3.控制台输出日志
        log.info("查询到符合条件的条目共{}条，现返回第{}页", teamPageInfo.getTotalSize(), teamPageInfo.getPageNow());
        return teamPageInfo;
    }

    @Override
    public Boolean addMember(List<Integer> userIds, Integer teamId) {
        //更新人数
        //插入信息
        userIds.forEach((userId)->{
            teamMapper.updateTeamJoinNum(teamId);
            userTeamMapper.insertUserTeam(userId, teamId);
        });
        return true;
    }

    @Override
    public Boolean newTeamByTeamIds(Integer creatorId, List<Integer> teamIds) {
        List<Integer> ids = new ArrayList<>();
        teamIds.forEach((teamId)->{
            ids.add(userTeamMapper.selectAnotherUserId(teamId, creatorId));
        });
        return newTeam(creatorId, ids);
    }

    @Override
    public List<Integer> getSubTeamId(Integer nowUserId, Integer teamId) {
        //取子团队的id
        //1.取该团队的所有成员，需要排除自己
        List<Integer> teamMembers = userTeamMapper.selectTeamMembers(teamId);
        teamMembers.remove(nowUserId);
        //2.取每个成员的私人团队id与当前用户的私人团队id的交集
        List<Integer> subTeamIds = new ArrayList<>();
        //2.1 取当前用户的所有私人团队id
        List<Integer> nowUserPrivateTeamIds = userTeamMapper.selectPrivateTeamIdById(nowUserId);
        List<Integer> tempNowUserPrivateTeamIds = new ArrayList<>(nowUserPrivateTeamIds);
        //2.2 遍历每个成员
        teamMembers.forEach((memberId)->{
            //2.3 取该成员的私人团队id
            List<Integer> memberPrivateTeamIds = userTeamMapper.selectPrivateTeamIdById(memberId);
            //2.4 取交集，暂存于tempNowUserPrivateTeamIds中
            tempNowUserPrivateTeamIds.retainAll(memberPrivateTeamIds);
            //2.5 转移到subTeamIds中
            subTeamIds.addAll(tempNowUserPrivateTeamIds);
            //2.6 重置tempNowUserPrivateTeamIds
            tempNowUserPrivateTeamIds.clear();
            tempNowUserPrivateTeamIds.addAll(nowUserPrivateTeamIds);
        });
        return subTeamIds;
    }

    @Override
    public Boolean addMemberByTeamIds(Integer nowUserId, Integer teamId, List<Integer> teamIds) {
        //1.根据当前用户和团队ids获取即将加入的用户的id
        List<Integer> userIds = new ArrayList<>();
        teamIds.forEach((id)->{
            userIds.add(userTeamMapper.selectAnotherUserId(id, nowUserId));
        });
        //2.添加入群
        addMember(userIds, teamId);
        return true;
    }


    private void paginate(List<Integer> teamIds, TeamPageInfo teamPageInfo, @Nullable boolean isPaginate){
        if(!teamIds.isEmpty()){
            //分页查询填充team信息
            //1.设置分页信息
            PageHelper.startPage(teamPageInfo.getPageNow(), isPaginate?6:teamIds.size());
            //2.查询数组中符合名字条件的team
            Page<Team> page = PageHelper.getLocalPage().doSelectPage(() -> teamMapper.selectByIdAndName(teamIds, teamPageInfo.getName()));
            teamPageInfo.setTableData(page.getResult());
            teamPageInfo.setTotalSize(page.getTotal());
            //2.1 查询到team后填充表格信息（头像teamImage、名字description、最后聊天时间message.sendTime）
            teamPageInfo.getTableData().forEach((team) -> {
                //若两者聊天，则信息为另一方的：
                if(team.getJoinNum() == 2){
                    //另一方的Id
                    Integer anotherUserId = userTeamMapper.selectAnotherUserId(team.getTeamId(), teamPageInfo.getId());
                    //另一方的头像和姓名
                    team.setTeamImage(userMapper.selectUserById(anotherUserId).getAvatarUrl());
                    team.setName(userMapper.selectUserById(anotherUserId).getUsername());
                }else{
                    //若为多人聊天，则信息为团队的
                    //无头像，使用默认头像
                    team.setTeamImage("http://localhost:8090/group.png");
                    //名字即为团队名
                }
                //2.1.3 填充最后聊天时间（暂存于updateTime中）
                team.setUpdateTime(messageMapper.selectLastSendTime(team.getTeamId()));
            });
            //2.2 让Team数组中的team按照最后聊天时间排序
            teamPageInfo.getTableData().sort(Comparator.comparing(Team::getUpdateTime, Comparator.nullsLast(Comparator.naturalOrder())));
        }else{
            teamPageInfo.setTotalSize(0L);
        }
    }
}
