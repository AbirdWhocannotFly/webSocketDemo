package com.bj58.payment.demo.socketServer;

import com.bj58.payment.demo.util.WebSocketMapUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * @author yangguang14
 * @date 2019/10/9.
 * ws://[Server 端 IP 或域名]:[Server 端口]/项目名/testChat/userid
 */
@ServerEndpoint(value = "/testChatGroup/{groupid}")
@Component
@Slf4j
public class MyWebGroupChatSocketServer {

    @Getter
    private Session session;
    @Getter
    private String groupid;

    /**
     * 连接建立后触发的方法
     */

    @OnOpen
    public void onOpen(@PathParam("groupid") String groupid ,Session session) throws IOException {
        this.session = session;
        this.groupid = groupid;
        log.info("onOpen groupid:{} 加入成员,sessionId：{}", groupid, session.getId());
        WebSocketMapUtil.putGroupChat(groupid, this);
        this.sendMessage("onOpen groupid:{" + groupid + "} 加入成员,sessionId：{" + session.getId() + "}");
    }
    /**
     * 连接关闭后触发的方法
     */
    @OnClose
    public void onClose() throws IOException {
        //从map中删除
        WebSocketMapUtil.removeChatGroup(groupid, session.getId());
        log.info("onClose groupid:{} 退出成员,sessionId：{}", groupid, session.getId());
        this.sendMessage("onClose groupid:{"+groupid+"} 退出成员,sessionId：{"+ session.getId()+"}");
    }
    /**
     * 接收到客户端消息时触发的方法
     */
    @OnMessage
    public void onMessage(String msg, Session session) throws Exception {
        //获取服务端到客户端的通道
        log.info("收到来自[{}]的消息,[{}]", session.getId(), msg);
        String result = msg.toString();
        //返回消息给Web Socket客户端（浏览器）
        this.sendMessage(session.getId(),"来自[" + session.getId() + "]的消息:" + msg);
    }

    /**
     * 给除本人外的所有人发送消息
     * @param id
     * @param msg
     */
    private void sendMessage(String id, String msg) throws IOException {
        for(MyWebGroupChatSocketServer socketServer : WebSocketMapUtil.getChatGroup(groupid)){
            if (socketServer.getSession().getId().equals(id)){
                continue;
            }
            socketServer.session.getBasicRemote().sendText(msg);
        }
    }

    /**
     * 群组消息
     * @param msg
     * @throws IOException
     */
    private void sendMessage(String msg) throws IOException {
        for(MyWebGroupChatSocketServer socketServer : WebSocketMapUtil.getChatGroup(groupid)){
                socketServer.session.getBasicRemote().sendText(msg);
        }

    }

    /**
     * 发生错误时触发的方法
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.info(session.getId() + "连接发生错误" + error.getMessage());
        error.printStackTrace();
    }

}
