package com.dbzhw.controller;

import com.dbzhw.common.CommonResult;
import com.dbzhw.constants.Constants;
import com.dbzhw.feign.MemberServiceFeign;
import com.dbzhw.utils.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;

@Controller
@Slf4j
public class IndexController {

    private static final String INDEX = "index";
    @Autowired
    private MemberServiceFeign memberServiceFeign;

    /**
     * 从Cookie中获取Token并换取用户信息
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(HttpServletRequest request) {
        // 1.获取Token
        String token = CookieUtil.getUid(request, Constants.USER_COOKIE_TOKEN);
        log.info("###客户端传过来的token:" + token);
        if (StringUtils.isEmpty(token))
            return "login";
        // 2.调用会员服务获取登录的user
        CommonResult result = memberServiceFeign.findTokenUser(token);
        if (!result.getRtnCode().equals(Constants.HTTP_RES_CODE_200)) {
            request.setAttribute("exception", "feignException");
            return INDEX;
        }
        LinkedHashMap userData = (LinkedHashMap) result.getData();
        String username = (String) userData.get("username");
        request.setAttribute("username", username);
        return INDEX;
    }
}
