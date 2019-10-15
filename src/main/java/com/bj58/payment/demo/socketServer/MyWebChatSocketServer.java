package com.bj58.payment.demo.socketServer;

import com.bj58.payment.demo.entity.ChatSocketMsg;
import com.bj58.payment.demo.util.ChatDecoder;
import com.bj58.payment.demo.util.HttpSessionConfigurator;
import com.bj58.payment.demo.util.WebSocketMapUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * @author yangguang14
 * @date 2019/10/9.
 * ws://[Server 端 IP 或域名]:[Server 端口]/项目名/testChat/userid
 */
@ServerEndpoint(value = "/testChat/{userid}",decoders = {ChatDecoder.class},configurator = HttpSessionConfigurator.class)
@Component
@Slf4j
public class MyWebChatSocketServer {

    private Session session;

    private String userid;

    /**
     * 连接建立后触发的方法
     */

    @OnOpen
    public void onOpen(@PathParam("userid") String userid ,Session session,EndpointConfig config) {
        this.session = session;
        this.userid = userid;
        HttpSession httpSession= (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        String userName = (String)httpSession.getAttribute("userName");
        log.info("onOpen userid:{},userName:{},sessionId：{}", userid,userName, session.getId());
        WebSocketMapUtil.putChat(userid, this);
    }
    /**
     * 连接关闭后触发的方法
     */
    @OnClose
    public void onClose() {
        //从map中删除
        WebSocketMapUtil.removeChat(this.userid);
        log.info("====== onClose sessionId: {}", session.getId());
    }
    /**
     * 接收到客户端消息时触发的方法
     */
    @OnMessage
    public void onMessage(ChatSocketMsg msg, Session session) throws Exception {
        //获取服务端到客户端的通道
        MyWebChatSocketServer myWebSocket = WebSocketMapUtil.getChat(userid);
        log.info("收到来自[{}]的消息,[{}]" ,userid , msg.toString());
        String result = msg.toString();
        //返回消息给Web Socket客户端（浏览器）
        myWebSocket.sendMessage(msg.getUserTo(), "来自["+userid+"]的消息:"+msg.getMsg());
    }

    /**
     * 单对单消息
     * @param userTo
     * @param msg
     * @throws IOException
     */
    private void sendMessage(String userTo, String msg) throws IOException {
        boolean offline = true;
        for(MyWebChatSocketServer socketServer : WebSocketMapUtil.getChatValues()){
            if (userTo.equals(socketServer.userid)){
                socketServer.session.getBasicRemote().sendText(msg);
                offline = false;
            }
        }
        if (offline){
            this.session.getBasicRemote().sendText("消息接收方["+userTo+"]不在线,消息发送失败");
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
