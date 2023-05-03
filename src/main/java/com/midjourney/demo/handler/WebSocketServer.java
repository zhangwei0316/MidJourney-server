package com.midjourney.demo.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/websocket")
@Component
@Slf4j
public class WebSocketServer {

    private static final RestTemplate REST_TEMPLATE = new RestTemplate();

    private static Map<String, WebSocketServer> MAP = new ConcurrentHashMap<>();

    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        MAP.put("1", this);
        log.info("WebSocket连接已经打开：{}", session);
    }

    @OnClose
    public void onClose() {
        log.info("WebSocket连接已经关闭");
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("收到客户端的消息: " + message);
        // 将消息发送到第三方接口b
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 设置请求体
        String requestBody = "{\"type\":\"generate\",\"prompt\":\"" + message + "\"}";
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // 发送请求
        String url = "http://localhost:16007/v1/trigger/midjourney-bot";
        REST_TEMPLATE.postForEntity(url, requestEntity, String.class);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("WebSocket连接发生错误: ", error);
    }

    // 发送消息到客户端
    public void sendMessage(String message, String id) throws IOException {
        if(this.session == null){
            this.session = MAP.get(id).session;
        }
        this.session.getBasicRemote().sendText(message);
    }

}

