package com.bj58.payment.demo.socketServer;

import com.alibaba.fastjson.JSONObject;
import com.bj58.payment.demo.util.WebSocketMapUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * @author yangguang14
 * @date 2019/10/9.
 * ws://[Server 端 IP 或域名]:[Server 端口]/项目名/testSocket
 */
@ServerEndpoint(value = "/testSocket")
@Component
@Slf4j
public class MyWebSocketServer {

    private Session session;

    /**
     * 连接建立后触发的方法
     */

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        log.info("onOpen sessionId：{}", session.getId());
        WebSocketMapUtil.put(session.getId(), this);
    }
    /**
     * 连接关闭后触发的方法
     */
    @OnClose
    public void onClose(Session session) {
        //从map中删除
        WebSocketMapUtil.remove(session.getId());
        log.info("====== onClose sessionId: {}" ,session.getId());
    }
    /**
     * 接收到客户端消息时触发的方法
     */
    @OnMessage
    public void onMessage(String params, Session session) throws Exception {
        //获取服务端到客户端的通道
        MyWebSocketServer myWebSocket = WebSocketMapUtil.get(session.getId());
        log.info("收到来自[{}]的消息,[{}]" ,session.getId() , params);
        String result = "收到来自" + session.getId() + "的消息" + params;
        //返回消息给Web Socket客户端（浏览器）
        myWebSocket.sendMessage(1, "success", result);
    }
    /**
     * 发生错误时触发的方法
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.info(session.getId() + "连接发生错误" + error.getMessage());
        error.printStackTrace();
    }

    public void sendMessage(int status, String message, Object datas) throws IOException {
        JSONObject result = new JSONObject();
        result.put("status", status);
        result.put("message", message);
        result.put("datas", datas);
        this.session.getBasicRemote().sendText(result.toString());
    }
}
