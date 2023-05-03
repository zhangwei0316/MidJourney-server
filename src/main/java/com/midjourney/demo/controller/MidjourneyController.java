package com.midjourney.demo.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.midjourney.demo.entity.DiscordMidjourney;
import com.midjourney.demo.handler.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
@Slf4j
public class MidjourneyController {

    @Autowired
    private WebSocketServer webSocketServer;

    @PostMapping("/image")
    @ResponseBody
    public void getImage(@RequestBody DiscordMidjourney midjourney) {
        log.info("生成图片：{}", midjourney);
        JSONObject jsonObject = midjourney.getDiscord();
        JSONArray attachments = jsonObject.getJSONArray("attachments");
        JSONObject attachment = attachments.getJSONObject(0);
        String url = attachment.getString("url");
        try {
            webSocketServer.sendMessage(url, "1");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
