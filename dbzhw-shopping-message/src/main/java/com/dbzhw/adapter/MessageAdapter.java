package com.dbzhw.adapter;

import com.alibaba.fastjson.JSONObject;

/**
 * 同一发送消息接口,发送短信,发送邮箱...
 */
public interface MessageAdapter {
    void sendMsg(JSONObject body);
}
