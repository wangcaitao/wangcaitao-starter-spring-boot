package com.wangcaitao.starter.redis;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author wangcaitao
 */
@Configuration
public class RedisUtils {

    /**
     * instance
     */
    private static RedisUtils instance;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * init
     */
    @PostConstruct
    public void init() {
        instance = this;
    }

    /**
     * get RedisTemplate object
     *
     * @return RedisTemplate object
     */
    public static RedisTemplate<String, Object> getRedisTemplate() {
        return instance.redisTemplate;
    }

    /**
     * 删除
     *
     * @param key key
     */
    public static void delete(String key) {
        getRedisTemplate().delete(key);
    }

    /**
     * 获取失效时间. 单位: 秒<br>
     * -2: key 不存在
     * -1: key 未设置失效时间
     *
     * @param key key
     * @return 失效时间
     */
    public static Long getExpire(String key) {
        return getRedisTemplate().getExpire(key);
    }

    /**
     * 获取失效时间. 单位: 自定义<br>
     * -2: key 不存在
     * -1: key 未设置失效时间
     *
     * @param key      key
     * @param timeUnit timeUnit
     * @return 失效时间
     */
    public static Long getExpire(String key, TimeUnit timeUnit) {
        return getRedisTemplate().getExpire(key, timeUnit);
    }

    /**
     * 设置失效时间. 单位: 秒
     *
     * @param key     key
     * @param timeout timeout
     */
    public static void expire(String key, long timeout) {
        getRedisTemplate().expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置指定失效时间, 具体到某一时刻.
     *
     * @param key  key
     * @param date date
     */
    public static void expireAt(String key, Date date) {
        getRedisTemplate().expireAt(key, date);
    }

    /**
     * 是否存在 key
     *
     * @param key key
     * @return 是否存在
     */
    public static boolean hasKey(String key) {
        Boolean result = getRedisTemplate().hasKey(key);

        return null == result ? false : result;
    }
}
