package com.bj58.payment.demo.entity;

import lombok.Data;

/**
 * @author yangguang14
 * @date 2019/10/10.
 */
@Data
public class ChatSocketMsg {

    private String userFrom;
    private String userTo;
    private String msg;

    @Override
    public String toString() {
        return new StringBuffer("")
                .append("向用户：【").append(userTo).append("】发送了一条消息，内容：")
                .append(msg).toString();
    }
}
