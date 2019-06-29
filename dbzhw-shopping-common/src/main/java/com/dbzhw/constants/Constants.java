package com.dbzhw.constants;

public interface Constants {
    // 响应请求成功
    String HTTP_RES_CODE_200_VALUE = "success";
    // 系统错误
    String HTTP_RES_CODE_500_VALUE = "fial";
    // 响应请求成功code
    Integer HTTP_RES_CODE_200 = 200;
    // 系统错误
    Integer HTTP_RES_CODE_500 = 500;

    // 发送邮件
    String SMS_MAIL = "email";

    // 用户token前缀
    String MEMBER_TOKEN = "MEMBER_TOKEN";
    // 支付token前缀
    String PAY_TOKEN = "PAY_TOKEN";

    // 用户token有效时间
    Long MEMBER_TOKEN_TIMEOUT = Long.valueOf(60 * 60 * 24 * 90);

    // 支付token有效时间
    Long PAY_TOKEN_TIMEOUT = Long.valueOf(60 * 15);


    String USER_COOKIE_TOKEN = "USER_COOKIE_TOKEN";
}
