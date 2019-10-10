package com.bj58.payment.demo.util;

import com.bj58.payment.demo.socketServer.MyWebChatSocketServer;
import com.bj58.payment.demo.socketServer.MyWebGroupChatSocketServer;
import com.bj58.payment.demo.socketServer.MyWebSocketServer;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author yangguang14
 * @date 2019/10/9.
 */
public class WebSocketMapUtil {
    public static ConcurrentMap<String, MyWebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, MyWebChatSocketServer> webChatSocketMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, Set<MyWebGroupChatSocketServer>> webGroupChatSocketMap = new ConcurrentHashMap<>();

    public static void put(String key, MyWebSocketServer myWebSocketServer) {
        webSocketMap.put(key, myWebSocketServer);
    }

    public static void putChat(String key, MyWebChatSocketServer myWebSocketServer) {
        webChatSocketMap.put(key, myWebSocketServer);
    }

    public static void putGroupChat(String groupid,MyWebGroupChatSocketServer myWebGroupChatSocketServer){
        synchronized (webGroupChatSocketMap){
            Set<MyWebGroupChatSocketServer> set = webGroupChatSocketMap.get(groupid);
            if (CollectionUtils.isEmpty(set)){
                set = new LinkedHashSet<>();
            }
            set.add(myWebGroupChatSocketServer);
            webGroupChatSocketMap.put(groupid,set);
        }
    }

    public static MyWebSocketServer get(String key) {
        return webSocketMap.get(key);
    }
    public static MyWebChatSocketServer getChat(String key) {
        return webChatSocketMap.get(key);
    }

    public static Set<MyWebGroupChatSocketServer> getChatGroup(String groupid){
        return webGroupChatSocketMap.get(groupid);
    }

    public static void remove(String key) {
        webSocketMap.remove(key);
    }
    public static void removeChat(String key) {
        webChatSocketMap.remove(key);
    }

    public static void removeChatGroup(String groupid, String key) {
        synchronized (webGroupChatSocketMap){
            Set<MyWebGroupChatSocketServer> set = webGroupChatSocketMap.get(groupid);
            if (CollectionUtils.isEmpty(set)){
                return;
            }
            for (MyWebGroupChatSocketServer socketServer : set){
                if (socketServer.getSession().getId().equals(key)){
                    set.remove(socketServer);
                    break;
                }
            }
        }
    }

    public static Collection<MyWebSocketServer> getValues() {
        return webSocketMap.values();
    }
    public static Collection<MyWebChatSocketServer> getChatValues() {
        return webChatSocketMap.values();
    }

}
