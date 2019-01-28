package com.aispread.admin.notice;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.aispread.admin.notice.bean.PushMessage;
import com.aispread.admin.notice.constants.SocketMessageType;
import com.aispread.admin.notice.dto.SendMessageRequest;
import com.alibaba.fastjson.JSON;
import com.redimybase.common.util.RedisUtils;
import com.redimybase.common.util.ToolUtil;
import com.redimybase.framework.listener.SpringContextListener;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Vim 2018/12/28 10:58
 *
 * @author Vim
 */
@Slf4j
@ServerEndpoint(value = "/websocket/{os}/{userId}")
@Component
public class WebSocketServer {
    @Autowired
    private RedisUtils redisUtils;

    /**
     * 线程安全的计数
     */
    private static final AtomicInteger CONNECTION_IDS = new AtomicInteger(0);

    /**
     * 客户端集合
     */
    private static ConcurrentHashMap<String, WebSocketServer> clients = new ConcurrentHashMap<>();
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    private String userId;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(@PathParam("userId") String userId, @PathParam("os") String os, Session session) throws IOException {
      /*  if (StringUtils.equals(SecurityUtil.getCurrentUserId(), userId)) {
            return;
        }*/
        if (StringUtils.isBlank(userId)) {
            return;
        }
        WebSocketServer webSocketServer = new WebSocketServer();
        webSocketServer.setUserId(userId);
        webSocketServer.setSession(session);

        //加入set中
        clients.put(userId + "_" + os, webSocketServer);

        log.info(String.format("[用户ID: %s]连接服务器！当前在线人数为 %s 人", userId, CONNECTION_IDS.incrementAndGet()));
        //根据当前的userId去遍历筛选一遍redis中存储的未发送消息
        //获取redis中未发送的属于该用户的信息列表
        if (redisUtils == null) {
            redisUtils = SpringContextListener.getBean(RedisUtils.class);
        }
        Set<String> unsentMsgList = redisUtils.keys(String.format("%s:%s:notice:*", os, userId));
        SendMessageRequest sendMessageRequest = new SendMessageRequest();
        sendMessageRequest.setType("systemMsg");
        sendMessageRequest.setOs(os);
        sendMessageRequest.setMessage("连接服务器成功");

        sendMessage(userId, os, JSON.toJSONString(sendMessageRequest), SocketMessageType.站内推送);
        for (String msgKey : unsentMsgList) {
            //发送消息
            sendMessage(userId, os, redisUtils.get(msgKey).toString(), SocketMessageType.站内推送);
            //从redis中删除已经发送的
            redisUtils.del(msgKey);
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("userId") String userId, @PathParam("os") String os) {
        //从set中删除
        clients.remove(userId + "_" + os);
        log.info(String.format("[用户ID: %s]与服务器断开连接！当前在线人数为 %s 人", userId, CONNECTION_IDS.decrementAndGet()));
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, @PathParam("os") String os, Session session) throws IOException {
        log.info(String.format("来自[%s]平台的消息: [%s]", os, message));

        //群发消息
        for (Map.Entry<String, WebSocketServer> c : clients.entrySet()) {
            WebSocketServer value = c.getValue();
            value.sendMessage(value.getUserId(), os, message, SocketMessageType.站内推送);
        }
    }

    /**
     * 群发自定义消息
     */
    public void sendInfo(Object messageObj, String os, List<String> targetList) throws IOException {

        PushMessage pushMessage = new PushMessage(messageObj);
        String message = JSON.toJSONString(pushMessage);
        log.info(message);
        if (clients.size() == 0) {
            for (String userId : targetList) {
                cacheMessage(userId, os, message);
            }
        } else {
            for (String userId : targetList) {
                WebSocketServer c = clients.get(userId + "_" + os);
                if (c != null) {
                    sendMessage(userId, os, message, SocketMessageType.站内推送);
                } else {
                    cacheMessage(userId, os, message);
                }
            }
        }
    }

    /**
     * 缓存消息
     */
    private void cacheMessage(String userId, String os, String message) {
        //存redis  Notice_UserID :  message

        String redisKey = String.format("%s:%s:notice:%s", os, userId, ToolUtil.getRandomString(4));
        redisUtils.set(redisKey, message);
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 消息发送
     *
     * @param message 需要发送的消息
     * @throws IOException
     */
    public void sendMessage(String userId, String os, String message, String typeFlag) throws IOException {
        WebSocketServer c = clients.get(userId + "_" + os);
        if (c == null) {
            return;
        }
        switch (typeFlag) {
            case SocketMessageType.站内推送:
                log.info(String.format("[推送类型: %s-平台: %s][目标用户ID: %s]发送消息: [%s]", SocketMessageType.站内推送, os, userId, message));
                c.getSession().getBasicRemote().sendText(message);
                break;
            case SocketMessageType.邮件:
                log.info(String.format("[推送类型: %s-平台: %s][目标用户ID: %s]发送消息: [%s]", SocketMessageType.站内推送, os, userId, message));
                //邮件中心发送
                break;
            default:
                log.error("类型错误");
                break;
        }
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return StringUtils.equals(this.userId, ((WebSocketServer) obj).userId);
    }
}