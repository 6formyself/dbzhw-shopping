package com.dbzhw.utils;

import com.dbzhw.constants.Constants;

import java.util.UUID;

public class TokenUtils {

    public static String getToken() {
        return Constants.MEMBER_TOKEN + UUID.randomUUID();
    }

    public static String getPayToken() {
        return Constants.PAY_TOKEN + UUID.randomUUID();
    }
}
