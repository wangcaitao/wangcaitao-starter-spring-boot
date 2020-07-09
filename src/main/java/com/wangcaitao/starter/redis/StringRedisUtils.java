package com.wangcaitao.starter.redis;

import com.wangcaitao.common.util.JacksonUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author wangcaitao
 */
@Configuration
public class StringRedisUtils {

    /**
     * instance
     */
    private static StringRedisUtils instance;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * init
     */
    @PostConstruct
    public void init() {
        instance = this;
    }

    /**
     * get StringRedisTemplate object
     *
     * @return StringRedisTemplate object
     */
    public static StringRedisTemplate getStringRedisTemplate() {
        return instance.stringRedisTemplate;
    }

    /**
     * get
     *
     * @param key key
     * @return value
     */
    public static String get(String key) {
        return get(key, String.class);
    }

    /**
     * get
     *
     * @param key   key
     * @param clazz clazz
     * @param <T>   T
     * @return value
     */
    public static <T> T get(String key, Class<T> clazz) {
        return JacksonUtils.convertObject(getStringRedisTemplate().opsForValue().get(key), clazz);
    }

    /**
     * list
     *
     * @param key   key
     * @param clazz clazz
     * @param <T>   T
     * @return value
     */
    public static <T> List<T> list(String key, Class<T> clazz) {
        return JacksonUtils.convertArray(getStringRedisTemplate().opsForValue().get(key), clazz);
    }

    /**
     * set
     *
     * @param key   key
     * @param value value
     */
    public static void set(String key, String value) {
        getStringRedisTemplate().opsForValue().set(key, value);
    }

    /**
     * set. 单位: 秒
     *
     * @param key     key
     * @param value   value
     * @param timeout timeout
     */
    public static void set(String key, String value, long timeout) {
        getStringRedisTemplate().opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * set. 单位: 自定义
     *
     * @param key      key
     * @param value    value
     * @param timeout  timeout
     * @param timeUnit timeUnit
     */
    public static void set(String key, String value, long timeout, TimeUnit timeUnit) {
        getStringRedisTemplate().opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 删除
     *
     * @param key key
     */
    public static void delete(String key) {
        getStringRedisTemplate().delete(key);
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
        return getStringRedisTemplate().getExpire(key);
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
        return getStringRedisTemplate().getExpire(key, timeUnit);
    }

    /**
     * 设置失效时间. 单位: 秒
     *
     * @param key     key
     * @param timeout timeout
     */
    public static void expire(String key, long timeout) {
        getStringRedisTemplate().expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置指定失效时间, 具体到某一时刻.
     *
     * @param key  key
     * @param date date
     */
    public static void expireAt(String key, Date date) {
        getStringRedisTemplate().expireAt(key, date);
    }

    /**
     * 是否存在 key
     *
     * @param key key
     * @return 是否存在
     */
    public static boolean hasKey(String key) {
        Boolean result = getStringRedisTemplate().hasKey(key);

        return null == result ? false : result;
    }
}
