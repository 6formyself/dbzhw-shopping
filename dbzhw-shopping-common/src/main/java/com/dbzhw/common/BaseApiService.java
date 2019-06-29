package com.dbzhw.common;

import com.dbzhw.constants.Constants;
import org.springframework.stereotype.Component;

/**
 * api的通用基类
 */
@Component
public class BaseApiService {

    /**
     * 通用返回结果
     *
     * @param code
     * @param msg
     * @param data
     * @return
     */
    protected CommonResult setResult(Integer code, String msg, Object data) {
        return new CommonResult(code, msg, data);
    }

    /**
     * 返回成功,不带data参数
     *
     * @return
     */
    protected CommonResult setResultSuccess() {
        return setResult(Constants.HTTP_RES_CODE_200, Constants.HTTP_RES_CODE_200_VALUE, null);
    }

    /**
     * 返回成功,带data参数
     *
     * @return
     */
    protected CommonResult setResultSuccess(Object data) {
        return setResult(Constants.HTTP_RES_CODE_200, Constants.HTTP_RES_CODE_200_VALUE, data);
    }

    /**
     * 返回成功,带msg参数
     *
     * @return
     */
    protected CommonResult setResultSuccess(String msg) {
        return setResult(Constants.HTTP_RES_CODE_200, msg, null);
    }

    /**
     * 返回成功,带msg和data参数
     *
     * @return
     */
    protected CommonResult setResultSuccess(String msg, Object data) {
        return setResult(Constants.HTTP_RES_CODE_200, msg, data);
    }

    /**
     * 返回失败,不带data错误数据
     *
     * @return
     */
    protected CommonResult setResultFailed() {
        return setResult(Constants.HTTP_RES_CODE_500, Constants.HTTP_RES_CODE_500_VALUE, null);
    }

    /**
     * 返回失败,带data错误数据
     *
     * @return
     */
    protected CommonResult setResultFailed(Object data) {
        return setResult(Constants.HTTP_RES_CODE_500, Constants.HTTP_RES_CODE_500_VALUE, data);
    }

    /**
     * 返回失败,带msg错误信息
     *
     * @return
     */
    protected CommonResult setResultFailed(String msg) {
        return setResult(Constants.HTTP_RES_CODE_500, msg, null);
    }

}
