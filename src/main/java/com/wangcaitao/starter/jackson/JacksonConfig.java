package com.wangcaitao.starter.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wangcaitao.common.util.JacksonUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangcaitao
 */
@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return JacksonUtils.OBJECT_MAPPER;
    }
}
