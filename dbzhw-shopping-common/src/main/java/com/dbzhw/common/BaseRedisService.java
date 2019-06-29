package com.dbzhw.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 对于redis中值的设置与获取
 */
@Component
public class BaseRedisService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void setString(String key, Object data) {
        setString(key, data, null);
    }

    /**
     * 向redis中添加数据
     *
     * @param key     键
     * @param data    数据
     * @param timeout 超时时间
     */
    public void setString(String key, Object data, Long timeout) {
        if (data instanceof String) {
            String value = (String) data;
            stringRedisTemplate.opsForValue().set(key, value);
        }
        if (timeout != null) {
            stringRedisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        }
    }

    /**
     * 获取redis中的值
     *
     * @param key 键
     * @return 值
     */
    public String getString(String key) {
        return (String) stringRedisTemplate.opsForValue().get(key);
    }

    public void delKey(String key) {
        stringRedisTemplate.delete(key);
    }
}
