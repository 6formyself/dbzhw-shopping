package com.dbzhw.common;

import lombok.Getter;
import lombok.Setter;

/**
 * 通用返回结果
 */
@Getter
@Setter
public class CommonResult {

    private Integer rtnCode;
    private String rtnMsg;
    private Object data;

    public CommonResult(Integer rtnCode, String rtnMsg, Object data) {
        this.rtnCode = rtnCode;
        this.rtnMsg = rtnMsg;
        this.data = data;
    }

    public CommonResult() {
    }
}
