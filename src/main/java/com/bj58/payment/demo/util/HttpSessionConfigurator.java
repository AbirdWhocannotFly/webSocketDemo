package com.bj58.payment.demo.util;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.UUID;

/**
 * @author yangguang14
 * @date 2019/10/14.
 */
public class HttpSessionConfigurator extends ServerEndpointConfig.Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        httpSession.setAttribute("userName", UUID.randomUUID().toString());
        sec.getUserProperties().put(HttpSession.class.getName(), httpSession);
    }
}
