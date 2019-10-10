package com.bj58.payment.demo.util;

import com.alibaba.fastjson.JSONObject;
import com.bj58.payment.demo.entity.ChatSocketMsg;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

/**
 * @author yangguang14
 * @date 2019/10/10.
 */
public class ChatDecoder implements Decoder.Text<ChatSocketMsg> {

    @Override
    public ChatSocketMsg decode(String s) throws DecodeException {
        return JSONObject.parseObject(s,ChatSocketMsg.class);
    }

    @Override
    public boolean willDecode(String s) {
        return true;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
