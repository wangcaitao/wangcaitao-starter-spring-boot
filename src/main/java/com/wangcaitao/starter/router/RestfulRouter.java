package com.wangcaitao.starter.router;

import com.wangcaitao.common.constant.HttpMethodEnum;
import lombok.Data;

/**
 * @author wangcaitao
 */
@Data
public class RestfulRouter {

    /**
     * 请求方式
     */
    private HttpMethodEnum method;

    /**
     * 请求地址
     */
    private String url;
}
