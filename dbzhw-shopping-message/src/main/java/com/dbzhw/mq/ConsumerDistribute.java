package com.dbzhw.mq;

import com.alibaba.fastjson.JSONObject;
import com.dbzhw.adapter.MessageAdapter;
import com.dbzhw.constants.Constants;
import com.dbzhw.email.MailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConsumerDistribute {
    @Autowired
    private MailService mailService;
    private MessageAdapter messageAdapter;

    /**
     * 监听消息队列 messages_queue
     *
     * @param json 通信协议,根据消息类型分类
     *             {
     *             "header": {
     *             "interfaceType": "email"
     *             },
     *             "content": {
     *             "mail": "wangkai19980103@163.com"
     *             }
     *             }
     */
    @JmsListener(destination = "messages_queue")
    public void distribute(String json) {
        log.info("####ConsumerDistribute###distribute() 消息服务平台接受 json参数:" + json);
        if (StringUtils.isEmpty(json)) {
            return;
        }
        JSONObject jsonObject = new JSONObject().parseObject(json);
        // 获取接口类型
        JSONObject header = jsonObject.getJSONObject("header");
        String interfaceType = header.getString("interfaceType");

        if (StringUtils.isEmpty(interfaceType)) {
            return;
        }
        // 发邮件类型
        if (interfaceType.equals(Constants.SMS_MAIL)) {
            messageAdapter = mailService;
        }
        if (messageAdapter == null) {
            return;
        }
        // 获取消息内容并发送
        JSONObject body = jsonObject.getJSONObject("content");
        messageAdapter.sendMsg(body);
    }

}
