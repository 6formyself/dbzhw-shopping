package com.dbzhw.manager;

import com.alipay.api.AlipayApiException;
import com.dbzhw.entity.PaymentInfo;

public interface PayManager {
    String payInfo(PaymentInfo paymentInfo) throws AlipayApiException;

}
