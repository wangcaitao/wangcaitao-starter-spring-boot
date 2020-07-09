package com.wangcaitao.starter.oss;

import com.wangcaitao.common.constant.CharConstant;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author wangcaitao
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "qiniu.oss")
@Slf4j
public class QiniuOssProperty {

    /**
     * accessKey
     */
    private String accessKey;

    /**
     * secretKey
     */
    private String secretKey;

    /**
     * bucket
     */
    private String bucket;

    /**
     * 基础路径. 约定结尾没有 /
     */
    private String baseUrl;

    /**
     * key 基础前缀. 非必填, 约定前后没有 /
     */
    private String baseKey;

    /**
     * init
     */
    @PostConstruct
    public void init() {
        if (null != baseUrl) {
            while (baseUrl.endsWith(CharConstant.SLASH)) {
                baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
            }
        }

        if (StringUtils.isEmpty(baseKey)) {
            baseKey = "";
        }

        while (baseKey.startsWith(CharConstant.SLASH)) {
            baseKey = baseKey.substring(1);
        }

        while (baseKey.endsWith(CharConstant.SLASH)) {
            baseKey = baseKey.substring(0, baseKey.length() - 1);
        }

        QiniuOssUtils.ACCESS_KEY = accessKey;
        QiniuOssUtils.SECRET_KEY = secretKey;
        QiniuOssUtils.BUCKET = bucket;
        QiniuOssUtils.BASE_URL = StringUtils.isEmpty(baseKey) ? baseUrl : baseUrl + CharConstant.SLASH + baseKey;
        QiniuOssUtils.BASE_KEY = baseKey;
    }
}
