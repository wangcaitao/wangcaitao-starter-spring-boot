package com.wangcaitao.starter.redis;

import com.wangcaitao.common.util.JacksonUtils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.StandardCharsets;

/**
 * @author wangcaitao
 */
public class JacksonRedisSerializer<T> implements RedisSerializer<T> {

    private Class<T> clazz;

    public JacksonRedisSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }

        return JacksonUtils.toJsonString(t).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        return JacksonUtils.parseObject(bytes, clazz);
    }
}
