package com.dbzhw.controller;

import com.dbzhw.common.CommonResult;
import com.dbzhw.constants.Constants;
import com.dbzhw.entity.UserEntity;
import com.dbzhw.feign.MemberServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class RegisterController {

    // 跳转页面
    private static final String REGISTER = "register";
    private static final String LOGIN = "login";
    @Autowired
    private MemberServiceFeign memberServiceFeign;

    /**
     * 跳转注册界面
     */
    @RequestMapping(value = "/registerView", method = RequestMethod.GET)
    public String registerView() {
        return REGISTER;
    }

    /**
     * 调用注册接口
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerUser(UserEntity user, HttpServletRequest request) {
        if (user == null)
            request.setAttribute("error", "请输出注册用户信息!");
        CommonResult result = memberServiceFeign.userRegister(user);
        if (!result.getRtnCode().equals(Constants.HTTP_RES_CODE_200)) {
            request.setAttribute("error", "注册失败!");
            return REGISTER;
        }
        return LOGIN;
    }

}
