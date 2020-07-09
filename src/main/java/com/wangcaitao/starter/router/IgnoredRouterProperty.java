package com.wangcaitao.starter.router;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * 忽略路由配置
 *
 * @author wangcaitao
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "router.ignored")
public class IgnoredRouterProperty {

    /**
     * 认证
     */
    private RestfulRouter[] authentications;

    /**
     * 授权
     */
    private RestfulRouter[] authorizations;

    @PostConstruct
    public void init() {
        IgnoredRouterUtils.AUTHENTICATIONS = authentications;
        IgnoredRouterUtils.AUTHORIZATIONS = authorizations;
    }
}
