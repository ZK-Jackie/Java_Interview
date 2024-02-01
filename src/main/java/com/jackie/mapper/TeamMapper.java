package com.jackie.mapper;

import com.jackie.pojo.Team;
import com.jackie.pojo.TeamPageInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface TeamMapper {

    List<Team> selectByIdAndName(List<Integer> ids, String name);

    void newTeam(Team team);

    List<Team> selectTeam(TeamPageInfo teamPageInfo);

    @Update("update team set join_num = join_num + 1 where team_id = #{teamId}")
    Boolean updateTeamJoinNum(Integer teamId);

}
