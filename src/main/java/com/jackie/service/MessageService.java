package com.jackie.service;

import com.jackie.pojo.Message;

import java.util.List;

public interface MessageService {

    Boolean addMessages(Message msg);

    Boolean withdrawMessage(Integer messageId);

    List<Message> getHistoryMessages(Integer userNowId, Integer teamId);

    Integer getTeamIdByMessageId(Integer messageId);

    Boolean cancelMessage(Integer messageId);
}
