package com.dbzhw.controller;

import com.alibaba.fastjson.JSONObject;
import com.dbzhw.common.TextMessage;
import com.dbzhw.utils.CheckUtil;
import com.dbzhw.utils.HttpClientUtil;
import com.dbzhw.utils.XmlUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

/**
 * 智能机器人
 */
@RestController
@Slf4j
public class DispatcherController {

    private final static String URL = "http://api.qingyunke.com/api.php?key=free&appid=0&msg=";

    /**
     * 微信验证
     */
    @RequestMapping(value = "/dispatch", method = RequestMethod.GET)
    public String dispatch(String signature, String timestamp, String nonce, String echostr) {
        log.info("#####" + signature);
        // 1.加密
        boolean isCheck = CheckUtil.checkSignature(signature, timestamp, nonce);
        if (!isCheck)
            return null;
        // 2.返回随机字符串
        return echostr;
    }

    /**
     * 微信动作请求
     */
    @RequestMapping(value = "/dispatch", method = RequestMethod.POST)
    public void dispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        // 1.将xml转换成map
        Map<String, String> parseMap = XmlUtils.parseXml(request);
        log.info("收到公众号消息:" + parseMap.toString());
        // 2.判断消息类型
        String msgType = parseMap.get("MsgType");
        // 3.文本,返回结果即可
        PrintWriter writer = response.getWriter();
        switch (msgType) {
            case "text":
                // 开发中账号
                String toUserName = parseMap.get("ToUserName");
                // 发送者账号
                String fromUserName = parseMap.get("FromUserName");
                // 收到的消息内容
                String content = parseMap.get("Content");
                // 调用智能机器人接口
                String resultStr = HttpClientUtil.doGet(URL + content);
                log.info(resultStr);
                JSONObject jsonObject = new JSONObject().parseObject(resultStr);
                Integer integer = jsonObject.getInteger("result");
                String textMessage;
                if (integer == null || integer != 0) {
                    textMessage = setMassageXml("亲,系统出错啦!", toUserName, fromUserName);
                } else {
                    String result = jsonObject.getString("content");
                    textMessage = setMassageXml(result, fromUserName, toUserName);
                }
                writer.println(textMessage);
                break;
        }
        writer.close();
    }

    /**
     * 消息转换成xml报文
     */
    private String setMassageXml(String content, String toUserName, String fromUserName) {
        TextMessage message = new TextMessage();
        message.setContent(content);
        message.setCreateTime(new Date().getTime());
        message.setFromUserName(fromUserName);
        message.setToUserName(toUserName);
        message.setMsgType("text");
        // 转换成xml
        return XmlUtils.messageToXml(message);
    }
}
