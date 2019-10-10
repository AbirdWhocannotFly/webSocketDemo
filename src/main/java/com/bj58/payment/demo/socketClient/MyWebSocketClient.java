package com.bj58.payment.demo.socketClient;

import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * @author yangguang14
 * @date 2019/10/9.
 */
@Slf4j
public class MyWebSocketClient extends WebSocketClient {

    public MyWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.info("------ MyWebSocket onOpen ------");
    }

    @Override
    public void onMessage(String message) {
        log.info("-------- 接收到服务端数据： {}--------",message);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        log.info("------ MyWebSocket onClose ------");
    }

    @Override
    public void onError(Exception e) {
        log.info("------ MyWebSocket onError ------");
    }
}
