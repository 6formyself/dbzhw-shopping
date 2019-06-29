package com.dbzhw.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.dbzhw.api.service.PayService;
import com.dbzhw.common.BaseApiService;
import com.dbzhw.common.BaseRedisService;
import com.dbzhw.common.CommonResult;
import com.dbzhw.constants.Constants;
import com.dbzhw.dao.PaymentInfoDao;
import com.dbzhw.entity.PaymentInfo;
import com.dbzhw.manager.PayManager;
import com.dbzhw.manager.impl.AliBaBaManagerImpl;
import com.dbzhw.utils.TokenUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PayServiceImpl extends BaseApiService implements PayService {

    @Autowired
    private PaymentInfoDao paymentInfoDao;
    @Autowired
    private BaseRedisService baseRedisService;
    @Autowired
    private AliBaBaManagerImpl aliBaBaManagerImpl;


    //@Autowired
    //private AliBaBaManagerImpl aliBaBaManagerImpl;


    /**
     * 创建支付令牌
     */
    @Override
    public CommonResult createPayToken(@RequestBody PaymentInfo paymentInfo) {

        // 1.生成支付信息,并将生成的id注入paymentInfo
        Integer effectLines = paymentInfoDao.savePaymentType(paymentInfo);
        if (effectLines <= 0)
            return setResultFailed("生成支付信息失败!");
        // 2.生成Token
        String paymentToken = TokenUtils.getPayToken();
        String paymentId = String.valueOf(paymentInfo.getId());
        // 3.将token与id存在redis中
        baseRedisService.setString(paymentToken, paymentId, Constants.PAY_TOKEN_TIMEOUT);
        // 4.返回token
        JSONObject tokenObj = new JSONObject();
        tokenObj.put("paymentToken", paymentToken);
        return setResultSuccess(tokenObj);
    }

    /**
     * 通过支付令牌查找支付信息
     */
    @Override
    public CommonResult getPaymentInfoId(@RequestParam String payToken) {
        // 1.验证Token是否为空
        if (StringUtils.isEmpty(payToken)) {
            return setResultFailed("token不能为空!");
        }
        // 2.获取payId,同时验证Token是否过期
        String payId = baseRedisService.getString(payToken);
        if (StringUtils.isEmpty(payId)) {
            return setResultFailed("支付已经超时!");
        }
        // 3.通过payId获取支付信息
        PaymentInfo paymentInfo = paymentInfoDao.getPaymentInfo(Long.parseLong(payId));
        if (paymentInfo == null) {
            return setResultFailed("未找到交易类型.");
        }
        // 4.返回html表单,根据支付类型选择支付接口
        Long typeId = paymentInfo.getTypeId();
        PayManager payManager = null;
        // 调用支付接口
        if (typeId == 1) {
            payManager = aliBaBaManagerImpl;
        }
        try {
            String payInfo = payManager.payInfo(paymentInfo);
            JSONObject payInfoJSON = new JSONObject();
            payInfoJSON.put("payInfo", payInfo);
            return setResultSuccess(payInfoJSON);
        } catch (AlipayApiException e) {
            return setResultFailed("支付错误!");
        }
    }
}
