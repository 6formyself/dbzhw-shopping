package com.dbzhw.api.service;

import com.dbzhw.common.CommonResult;
import com.dbzhw.entity.PaymentInfo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/pay")
public interface PayService {

    /**
     * 创建支付Token
     */
    @RequestMapping("/createPayToken")
    CommonResult createPayToken(@RequestBody PaymentInfo paymentInfo);

    /**
     * 根据支付token获取订单id
     */
    @RequestMapping("/getPaymentInfoId")
    CommonResult getPaymentInfoId(@RequestParam String patToken);
}
