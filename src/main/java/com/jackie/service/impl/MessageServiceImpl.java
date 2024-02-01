package com.jackie.service.impl;

import com.jackie.mapper.MessageMapper;
import com.jackie.pojo.Message;
import com.jackie.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private UserTeamServiceImpl userTeamService;

    @Override
    public Boolean addMessages(Message msg) {   //添加消息
        //msg中teamId一定不为空
        if(msg.getTeamId() == null) {
            log.error("消息teamId为空");
            return null;
        }
        //1.从teamId中获得信息接收者（不包括自己），删减多余字符
        List<Integer> receiversId = userTeamService.getTeamMembers(msg.getTeamId());
        receiversId.remove(msg.getSendUserId());
        msg.setContent(msg.getContent().trim());
        //2.制作Message对象
        if (receiversId.size() > 1) {
            //2.1 单人情况下：
            msg.setReceiveUserId(receiversId.get(0));
            msg.setReceiveType((short) 1);
            msg.setSendType((short) 1);
            msg.setType(1);
        } else {
            //2.2 多人情况下：
            msg.setReceiveUserId(null);
            msg.setReceiveType((short) 0);
            msg.setSendType((short) 0);
            msg.setType(0);
        }
        msg.setSendTime(LocalDateTime.now());
        return messageMapper.insertMessage(msg);
    }

    @Override
    public Boolean withdrawMessage(Integer messageId) {
        return messageMapper.withdrawMessage(messageId);
    }

    @Override
    public List<Message> getHistoryMessages(Integer userNowId, Integer teamId) {
        //1.作已读标记，没读过的才做已读标记
        messageMapper.updateIsRead(userNowId,teamId);
        //2.获取历史消息并返回
        return messageMapper.selectHistory(teamId);
    }

    @Override
    public Integer getTeamIdByMessageId(Integer messageId) {
        return messageMapper.selectTeamIdByMessageId(messageId);
    }


    @Override
    public Boolean cancelMessage(Integer messageId) {
        return messageMapper.updateIsCancel(messageId);
    }

}
