package com.bj58.payment.demo.socketClient;

import lombok.extern.slf4j.Slf4j;
import org.java_websocket.WebSocket;
import org.junit.Test;

import java.net.URI;

/**
 * @author yangguang14
 * @date 2019/10/9.
 */
@Slf4j
public class MyWebSocketClientTest {

    @Test
    public void testSend() throws Exception{
        URI uri = new URI("ws://localhost:8800/demo/testSocket");
        MyWebSocketClient myClient = new MyWebSocketClient(uri);
        myClient.connect();
        while (!myClient.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
            log.debug("连接中···请稍后");
            Thread.sleep(1000);
        }
        // 往websocket服务端发送数据
        myClient.send("客户端发送了一条消息。。");
        int times = 0;
        while (times<30){
            Thread.sleep(1000);
            times++;
        }
        myClient.close();
    }

}