package com.dbzhw.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Destination;

/**
 * 用户注册完毕后向消息服务发送消息
 */
@Service
public class RegisterMailboxProducer {
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    /**
     * 发送邮件
     *
     * @param destination 要发送的队列
     * @param json        消息,遵循通信协议(接口类型和内容)
     */
    public void sendMsg(Destination destination, String json) {
        jmsMessagingTemplate.convertAndSend(destination, json);
    }

}
