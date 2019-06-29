package com.dbzhw.controller;

import com.dbzhw.common.CommonResult;
import com.dbzhw.constants.Constants;
import com.dbzhw.entity.UserEntity;
import com.dbzhw.feign.MemberServiceFeign;
import com.dbzhw.utils.CookieUtil;
import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.oauth.Oauth;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;

@Controller
@Slf4j
public class LoginController {
    // 跳转页面
    private static final String LOGIN = "login";
    private static final String ERROR = "error";
    private static final String RELATION = "relation";
    // 重定向到首页
    private static final String INDEX = "redirect:/";
    @Autowired
    private MemberServiceFeign memberServiceFeign;

    /**
     * 登录界面
     */
    @RequestMapping(value = "/loginView", method = RequestMethod.GET)
    public String loginView() {
        return LOGIN;
    }

    /**
     * 用户登录,用户名密码登录
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(UserEntity user, HttpServletRequest request, HttpServletResponse response) {
        // 1.校验参数
        // 2.调用登录接口,获取Token
        CommonResult result = memberServiceFeign.login(user);
        if (!result.getRtnCode().equals(Constants.HTTP_RES_CODE_200)) {
            request.setAttribute("error", result.getRtnMsg());
            return LOGIN;
        }
        LinkedHashMap data = (LinkedHashMap) result.getData();
        String memberToken = (String) data.get("memberToken");
        // 3.将Token存放在cookie中
        CookieUtil.addCookie(response, Constants.USER_COOKIE_TOKEN, memberToken, Integer.valueOf(String.valueOf(Constants.MEMBER_TOKEN_TIMEOUT)));
        return INDEX;
    }

    /**
     * 获取qq授权链接,qq授权界面
     */
    @RequestMapping("/localQQLogin")
    public String localQQLogin(HttpServletRequest request) throws QQConnectException {
        String authorizeURL = new Oauth().getAuthorizeURL(request);
        return "redirect:" + authorizeURL;
    }

    /**
     * 授权后的回调地址,使用openid获取用户登录
     */
    @RequestMapping("/qqLoginCallback")
    public String qqLoginCallback(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession)
            throws QQConnectException {
        // 1.获取accessToken
        AccessToken accessTokenObj = new Oauth().getAccessTokenByRequest(request);
        if (accessTokenObj == null) {
            request.setAttribute("error", "qq授权失败!");
            return ERROR;
        }
        String accessToken = accessTokenObj.getAccessToken();
        if (StringUtils.isEmpty(accessToken)) {
            request.setAttribute("error", "qq授权失败!");
            return ERROR;
        }
        // 2.获取openid
        OpenID openIdObj = new OpenID(accessToken);
        String userOpenID = openIdObj.getUserOpenID();
        // 3.通过openid获取用户信息
        CommonResult openIdUser = memberServiceFeign.findOpenidUser(userOpenID);
        // 用戶沒有关联QQ账号
        if (openIdUser.getRtnCode().equals(201)) {
            // 跳转到关联页面
            httpSession.setAttribute("qqOpenid", userOpenID);
            return RELATION;
        }
        // 如果用户关联账号,将Token存入Cookie并重定向到index
        LinkedHashMap dataMap = (LinkedHashMap) openIdUser.getData();
        String memberToken = (String) dataMap.get("memberToken");
        // 4.将Token存放在cookie中
        CookieUtil.addCookie(response, Constants.USER_COOKIE_TOKEN, memberToken, Integer.valueOf(String.valueOf(Constants.MEMBER_TOKEN_TIMEOUT)));
        return INDEX;
    }

    /**
     * 绑定qq的openid并登录
     */
    @RequestMapping(value = "/qqRelation", method = RequestMethod.POST)
    public String qqRelation(UserEntity user, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        // 1.获取openid
        String openid = (String) httpSession.getAttribute("qqOpenid");
        if (StringUtils.isEmpty(openid)) {
            request.setAttribute("error", "openid不存在!");
            return ERROR;
        }
        user.setOpenid(openid);
        // 2.调用qqLogin接口绑定qq的openid
        CommonResult result = memberServiceFeign.qqLogin(user);
        // 3.将返回的Token存入Cookie
        if (!result.getRtnCode().equals(Constants.HTTP_RES_CODE_200)) {
            request.setAttribute("error", "feignError!");
            return ERROR;
        }
        LinkedHashMap data = (LinkedHashMap) result.getData();
        String memberToken = (String) data.get("memberToken");
        CookieUtil.addCookie(response, Constants.USER_COOKIE_TOKEN, memberToken, Integer.valueOf(String.valueOf(Constants.MEMBER_TOKEN_TIMEOUT)));
        // 4.重定向至index进行自动登录
        return INDEX;
    }
}
