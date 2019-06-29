package com.dbzhw.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dbzhw.api.service.MemberService;
import com.dbzhw.common.BaseApiService;
import com.dbzhw.common.BaseRedisService;
import com.dbzhw.common.CommonResult;
import com.dbzhw.constants.Constants;
import com.dbzhw.dao.MemberDao;
import com.dbzhw.entity.UserEntity;
import com.dbzhw.mq.RegisterMailboxProducer;
import com.dbzhw.utils.MD5Util;
import com.dbzhw.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 会员服务实现类
 */
@RestController
@Slf4j
public class MemberServiceImpl extends BaseApiService implements MemberService {

    @Autowired
    private MemberDao memberDao;
    @Autowired
    private RegisterMailboxProducer registerMailboxProducer;
    @Autowired
    private BaseRedisService baseRedisService;
    @Value("${messages.queue}")
    private String MESSAGE_QUEUE;

    /**
     * 用户查询根据id
     *
     * @param id 用户id
     */
    @Override
    public CommonResult findByUserId(Long id) {
        UserEntity user = memberDao.findByID(id);
        if (user == null)
            return setResultFailed("未查到用户信息!");
        return setResultSuccess(user);
    }

    /**
     * 用户注册
     *
     * @param user 注册的用户
     */
    @Override
    public CommonResult userRegister(@RequestBody UserEntity user) {
        String password = user.getPassword();
        if (StringUtils.isEmpty(password))
            setResultFailed("密码不能为空!");
        String newPassword = MD5Util.MD5(password);
        user.setPassword(newPassword);
        user.setCreated(new Date());
        user.setUpdated(new Date());
        Integer effectLine = memberDao.insertUser(user);
        if (effectLine <= 0)
            return setResultFailed("注册用户失败!");
        // 采用MQ异步发送邮件
        String email = user.getEmail();
        String messageJson = message(email);
        log.info("######email:{},messageJson:{}", email, messageJson);
        sendMsg(messageJson);
        return setResultSuccess("注册成功!");
    }

    /**
     * 用户登录
     *
     * @param user 登录的用户
     */
    @Override
    public CommonResult login(@RequestBody UserEntity user) {
        // 1.验证参数
        String username = user.getUsername();
        String password = user.getPassword();
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password))
            return setResultFailed("用户名密码为空!");
        password = MD5Util.MD5(password);
        // 2.数据库查找账号密码
        UserEntity loginUser = memberDao.login(username, password);
        if (loginUser == null)
            return setResultFailed("账号密码错误!");
        return setLogin(loginUser);
    }

    /**
     * 根据token查询用户
     */
    @Override
    public CommonResult findTokenUser(@RequestParam("token") String token) {
        // 1.验证参数
        if (StringUtils.isEmpty(token))
            return setResultFailed("token不能为空!");
        // 2.去redis中获取userId
        String userIdStr = baseRedisService.getString(token);
        if (StringUtils.isEmpty(userIdStr))
            return setResultFailed("token过期或无效!");
        Long userId = Long.parseLong(userIdStr);
        // 3.根据userId去数据库中查询用户
        UserEntity user = memberDao.findByID(userId);
        if (user == null)
            return setResultFailed("未找到用户信息!");
        // 去除密码
        user.setPassword(null);
        return setResultSuccess(user);
    }

    /**
     * openid查找qq信息
     */
    @Override
    public CommonResult findOpenidUser(@RequestParam("openid") String openid) {
        // 根据openid查询用户,查询成功则说明绑定过,并将Token返回且存入redis
        // 1.验证参数,用户是否绑定qq
        if (StringUtils.isEmpty(openid))
            return setResultFailed("openid为空!");
        // 2.使用openid查询用户信息
        UserEntity user = memberDao.findOpenidUser(openid);
        // 3.没有查询到用户信息,说明用户没有绑定qq
        if (user == null)
            return setResult(201, "用户未绑定qq!", null);
        // 4.redis存入Token与userId
        return setLogin(user);
    }

    /**
     * qq登录:关联openid,已有账号关联
     */
    @Override
    public CommonResult qqLogin(@RequestBody UserEntity user) {
        // 前端接受到201,则调该接口,用于绑定openid
        // 1.验证参数
        String username = user.getUsername();
        if (StringUtils.isEmpty(username)) {
            return setResultFailed("用戶名称不能为空!");
        }
        String password = user.getPassword();
        if (StringUtils.isEmpty(password)) {
            return setResultFailed("密码不能为空!");
        }
        // 2.根据username和password查询用户bindUser
        String newPassword = MD5Util.MD5(password);
        UserEntity bindUser = memberDao.login(username, newPassword);
        if (bindUser == null) {
            return setResultFailed("账号或者密码不正确");
        }
        // 3.设置openid
        String openid = user.getOpenid();
        Integer userId = bindUser.getId();
        Integer effectLine = memberDao.updateUserOpenid(userId, openid);
        if (effectLine <= 0)
            setResultFailed("关联失败!");
        // 4.redis存入Token和userId并返回Token
        return setLogin(bindUser);
    }

    /**
     * 创建消息json
     *
     * @param mail 要发送的email
     */
    private String message(String mail) {
        // 根
        JSONObject root = new JSONObject();
        // 头
        JSONObject header = new JSONObject();
        header.put("interfaceType", "email");
        // 内容
        JSONObject content = new JSONObject();
        content.put("mail", mail);
        // 组装
        root.put("header", header);
        root.put("content", content);
        return root.toJSONString();
    }

    /**
     * 向messages_queue队列中发送消息
     */
    private void sendMsg(String json) {
        ActiveMQQueue activeMQQueue = new ActiveMQQueue(MESSAGE_QUEUE);
        registerMailboxProducer.sendMsg(activeMQQueue, json);
    }

    /**
     * 登录时,查询到用户后,生成token
     */
    private CommonResult setLogin(UserEntity loginUser) {
        // 3.账号密码正确生成token
        String token = TokenUtils.getToken();
        Integer userId = loginUser.getId();
        // 4.将userId跟token存入redis
        log.info("####token:" + token + ",userId:" + userId);
        baseRedisService.setString(token, String.valueOf(userId), Constants.MEMBER_TOKEN_TIMEOUT);
        // 5.返回token
        JSONObject memberToken = new JSONObject();
        memberToken.put("memberToken", token);
        return setResultSuccess(memberToken);
    }
}
