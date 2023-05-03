package com.midjourney.demo.entity;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

@Data
public class DiscordMidjourney {

    private String type;

    private String content;

    private JSONObject discord;
}
