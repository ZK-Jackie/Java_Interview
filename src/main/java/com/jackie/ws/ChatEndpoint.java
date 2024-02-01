package com.jackie.ws;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jackie.configuration.GetHttpSessionConfigurator;
import com.jackie.pojo.Message;
import com.jackie.pojo.Result;
import com.jackie.service.MessageService;
import com.jackie.service.TeamService;
import com.jackie.service.UserService;
import com.jackie.service.UserTeamService;
import com.jackie.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ConcurrentReferenceHashMap;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;

@Slf4j
@Component
@ServerEndpoint(value = "/chat", configurator = GetHttpSessionConfigurator.class)
public class ChatEndpoint {
    private static UserService userService;
    private static TeamService teamService;
    private static UserTeamService userTeamService;
    private static MessageService messageService;

    @Autowired
    public void setUserService(UserService userService) {
        ChatEndpoint.userService = userService;
    }

    @Autowired
    public void setTeamService(TeamService teamService) {
        ChatEndpoint.teamService = teamService;
    }

    @Autowired
    public void setUserTeamService(UserTeamService userTeamService) {
        ChatEndpoint.userTeamService = userTeamService;
    }

    @Autowired
    public void setMessageService(MessageService messageService) {
        ChatEndpoint.messageService = messageService;
    }


    //在线用户表，记录用户名&会话
    private static final Map<Integer, Session> onlineUsers = new ConcurrentReferenceHashMap<>();
    //当前用户Id，session
    private Integer idNow;
    private Session sessionNow;
    //同步锁，本程序未用到
    private static final Map<Integer, Object> syncLock = new ConcurrentReferenceHashMap<>();

    //建立会话时调用，记下当前会话并广播该用户上线
    @OnOpen
    public void onOpen(Session session, @NotNull EndpointConfig config) {
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        //1.将当前登录的人的会话存入在线用户表
        String jwtToken = (String) httpSession.getAttribute("token");
        Integer id = (Integer) JwtUtils.parseJwt(jwtToken).get("id");
        onlineUsers.put(id, session);
        this.idNow = id;
        this.sessionNow = session;
        //1.1 为每个用户准备一把锁
        syncLock.put(id, new Object());
        //2.广播该用户上线
        //2.1 广播新上线的用户
        Result onLiveResult = new Result(2, id + "上线了", onlineUsers.keySet());//0-错误，1-成功，2-上线广播，3-信息撤回，4-聊天信息，5-下线广播，6-其他
        broadcast(onLiveResult);
//        log.info("用户{}上线了", id);
        //2.2 广播用户列表
//        HashMap<Integer, String> teamList = new HashMap<>();
//        Result nowLiveResult = new Result(3, "当前用户列表", onlineUsers.keySet());
//        broadcast(nowLiveResult, sessionNow);
    }

    //发送信息时调用
    @OnMessage
    public void onMessage(String message) {
        //三种情况：一般——1.接收到信息，2.要发送信息[4]；不一般——3.要撤回某信息[3]
        //0.JSON转Map，前端将传入：teamId，content，sendUserId
        Map<String, Object> jsonMsg = JSON.parseObject(message, Map.class);
        Message exchangeMsg;
        //1.解析message.code字段，获取前端意图
        if (jsonMsg.get("code").equals(4)) {    //发送/接收信息
            /*  信息的后端接收信息样式：
            * {
            *    code: 4,
            *    sendUserId: that.loginData.id,
            *    teamId: that.currentRow.teamId,
            *    content: that.textArea,
            * };
            * */
            //1.1传递信息信息
            jsonMsg.remove("code");
            exchangeMsg = JSON.parseObject(message, Message.class);
            //2.1移交传递信息方法
            exchangeMessage(exchangeMsg);
        } else if (jsonMsg.get("code").equals(3)) {   //撤回信息
            /* 撤回信息的后端接收信息样式：
             *   {
             *       code: 3,
             *       sendUserId: 撤回的用户的id,
             *       username: 撤回的用户的用户名,
             *       messageId: 要撤回的信息的id,
             *   }*/
            jsonMsg.remove("code");
            //1.2撤回信息
            Integer messageId = (Integer) jsonMsg.get("messageId");
            //2.移交撤回信息方法
            withdrawMessage(messageId, (String) jsonMsg.get("username"));
        }
    }

    @OnClose
    public void onClose(Session session) {
        log.info("用户{}下线了", this.idNow);
        //1.整理在线列表
        onlineUsers.remove(this.idNow);
        //2.广播该用户下线+广播在线用户列表
        Result offLiveResult = new Result(5, this.idNow + "下线了", onlineUsers.keySet());//0-错误，1-成功，2-上线广播，3-用户列表，4-聊天信息，5-下线广播
        broadcast(offLiveResult);
        //3.移除锁
        syncLock.remove(this.idNow);
    }

    @OnError
    public void onError(Session session, @NotNull Throwable error) {
        log.info("用户{}出现错误", this.idNow);
        Result errorResult;
        errorResult = new Result(0, "未知错误", null);//0-错误，1-成功，2-上线广播，3-用户列表，4-聊天信息，5-下线广播
        broadcast(errorResult);
        log.info(error.getMessage());
    }

    public void broadcast(Object message) { //全局广播信息，很容易产生并发问题，使用同步锁
        JSONObject json = (JSONObject) JSONObject.toJSON(message);
        onlineUsers.forEach((k, v) -> {
            //遍历在线列表发给所有人上线信息，注意同步
            try{
                v.getBasicRemote().sendText(json.toJSONString());
            }catch (Exception e){
                log.info("152"+e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private static void broadcast(Object message, Integer aim) throws IOException {    //使用id的一对一广播信息
        JSONObject json = (JSONObject) JSONObject.toJSON(message);
        onlineUsers.get(aim).getBasicRemote().sendText(json.toJSONString());
    }

    private void broadcast(Result withdrawResult, List<Integer> receiversId) {  //使用id列表的一对多广播信息
        receiversId.forEach((id) -> {
            try {
                broadcast(withdrawResult, id);
            } catch (Exception e) {
                log.info("168"+e.getMessage());
                Result errorResult;//0-错误，1-成功，2-上线广播，3-信息撤回，4-聊天信息，5-下线广播
                if (e instanceof NullPointerException) {
                    errorResult = new Result(0, "聊天对象不在线", null);
                } else {
                    errorResult = new Result(0, "未知错误", null);
                }
                try {
                    broadcast(errorResult, idNow);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void exchangeMessage(Message exchangeMsg) {
        //1.添加信息到数据库，并获得信息ID
        messageService.addMessages(exchangeMsg);
        //2.制作Result对象信息
        /*
         * 转发信息的后端返回信息样式：
         * {
         *   code: 4,
         *   message:"聊天信息",
         *   data:"{{信息}}"
         * }
         * */
        Result msgResult = new Result(4, "聊天信息", exchangeMsg);//0-错误，1-成功，2-上线广播，3-用户列表，4-聊天信息，5-下线广播
        //3.给接受者发送信息（包括自己）
        List<Integer> receiversId = userTeamService.getTeamMembers(exchangeMsg.getTeamId());
        broadcast(msgResult, receiversId);
    }


    private void withdrawMessage(Integer messageId, String withdrawUsername) {
        //1.数据库中处理并获得该信息的teamId
        messageService.cancelMessage(messageId);
        Integer teamId = messageService.getTeamIdByMessageId(messageId);
        //2.制作Result对象
        /*
         * 撤回信息的后端返回信息样式：
         * {
         *   code: 3,
         *   message:"撤回信息",
         *   data:{
         *       username: 撤回的用户的用户名,
         *       messageId: 要撤回的信息的id,
         *   }
         * }
         * */
        Map<String, String> withdrawData = new HashMap<>();
        withdrawData.put("username", withdrawUsername);
        withdrawData.put("messageId", messageId.toString());
        Result withdrawResult = new Result(3, "撤回信息", JSON.toJSONString(withdrawData));//0-错误，1-成功，2-上线广播，3-信息撤回，4-聊天信息，5-下线广播
        //3.team内广播（包括自己）
        List<Integer> receiversId = userTeamService.getTeamMembers(teamId);
        broadcast(withdrawResult, receiversId);
    }

}
