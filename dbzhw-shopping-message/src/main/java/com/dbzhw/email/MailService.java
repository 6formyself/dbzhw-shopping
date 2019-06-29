package com.dbzhw.email;

import com.alibaba.fastjson.JSONObject;
import com.dbzhw.adapter.MessageAdapter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MailService implements MessageAdapter {
    @Value("${msg.subject}")
    private String subject;
    @Value("${msg.text}")
    private String text;
    @Value("${spring.mail.username}")
    private String myEmail;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendMsg(JSONObject body) {
        // 获取邮箱
        String mail = body.getString("mail");
        if (StringUtils.isEmpty(mail)) {
            return;
        }
        log.info("消息发送给mail:{}", mail);

        // 邮件消息
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        // 发送 授权账号--->注册账号
        simpleMailMessage.setFrom(myEmail);
        simpleMailMessage.setTo(mail);
        // 标题
        simpleMailMessage.setSubject(subject);
        // 内容
        simpleMailMessage.setText(text.replace("{}", mail));
        // 发送消息
        mailSender.send(simpleMailMessage);
    }

}
