package com.jackie.mapper;

import com.jackie.pojo.Message;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface MessageMapper {

    @Select("select max(send_time) from message where team_id = #{teamId}")
    LocalDateTime selectLastSendTime(Integer teamId);

    @Insert("insert into message(team_id, send_user_id, receive_user_id, send_type, type, receive_type, content, send_time) values(#{teamId}, #{sendUserId}, #{receiveUserId}, #{sendType}, #{type}, #{receiveType}, #{content}, #{sendTime})")
    @Options(useGeneratedKeys = true, keyProperty = "messageId", keyColumn = "message_id")
    Boolean insertMessage(Message msg);

    @Select("select * from message where team_id = #{teamId} and is_cancel = 0")
    List<Message> selectHistory(Integer teamId);

    @Update("update message set is_cancel = 1, cancel_time = curdate() where message_id = #{messageId}")
    Boolean withdrawMessage(Integer messageId);

    @Select("select team_id from message where message_id = #{messageId}")
    Integer selectTeamIdByMessageId(Integer messageId);

    @Update("update message set is_cancel = 1, cancel_time = curdate() where message_id = #{messageId} and is_cancel = 0")
    Boolean updateIsCancel(Integer messageId);

    @Update("update message set is_read = 1, read_time = curdate() where receive_user_id = #{userNowId} and team_id = #{teamId} and is_read = 0")
    Boolean updateIsRead(Integer userNowId, Integer teamId);
}
