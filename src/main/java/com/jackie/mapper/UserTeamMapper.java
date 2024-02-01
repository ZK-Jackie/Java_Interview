package com.jackie.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface UserTeamMapper {
    @Select("select team_id from user_team where user_id = #{id} and is_delete = 0")
    List<Integer> selectTeamIdById(Integer id);

    @Insert("insert into user_team(team_id, user_id, create_time) values(#{newTeamId}, #{memberId}, #{now})")
    void addUserTeam(@Param("newTeamId") Integer newTeamId, @Param("memberId") Integer memberId, @Param("now") LocalDateTime now);

    @Select("select user_id from user_team where team_id = #{teamId} and user_id != #{id}")
    Integer selectAnotherUserId(@Param("teamId")Integer teamId, @Param("id")Integer id);

    @Select("select user_id from user_team where team_id = #{teamId}")
    List<Integer> selectMemberIdByTeamId(Integer teamId);

    @Select("select team_id from user_team where user_id = #{id} and is_delete = 0 and team_id in (select team_id from team where join_num = 2)")
    List<Integer> selectPrivateTeamIdById(Integer id);

    @Insert("insert into user_team(user_id, team_id) values(#{userId}, #{teamId})")
    Boolean insertUserTeam(Integer userId, Integer teamId);

    @Select("select user_id from user_team where team_id = #{teamId} and is_delete = 0")
    List<Integer> selectTeamMembers(Integer teamId);
}
