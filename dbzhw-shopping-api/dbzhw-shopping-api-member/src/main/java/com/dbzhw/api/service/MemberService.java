package com.dbzhw.api.service;

import com.dbzhw.common.CommonResult;
import com.dbzhw.entity.UserEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 会员服务接口
 */
@RequestMapping("/member")
public interface MemberService {

    /**
     * 根据id查找用户
     */
    @RequestMapping("/findByUserId")
    CommonResult findByUserId(Long id);

    /**
     * 用户注册
     */
    @RequestMapping("/register")
    CommonResult userRegister(@RequestBody UserEntity user);


    /**
     * 用户登录
     */
    @RequestMapping("/login")
    CommonResult login(@RequestBody UserEntity user);

    /**
     * 根据token查找用户
     */
    @RequestMapping("/findTokenUser")
    CommonResult findTokenUser(@RequestParam("token") String token);

    /**
     * 根据openid查找qq用户信息
     */
    @RequestMapping("/findOpenIdUser")
    CommonResult findOpenidUser(@RequestParam("openid") String openid);

    /**
     * qq登录
     */
    @RequestMapping("/qqLogin")
    CommonResult qqLogin(@RequestBody UserEntity user);
}
