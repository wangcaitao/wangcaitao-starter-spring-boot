package com.wangcaitao.starter.filter;

import com.wangcaitao.common.constant.CommonErrorEnum;
import com.wangcaitao.common.constant.PageConstant;
import com.wangcaitao.common.util.HttpServletResponseUtils;
import com.wangcaitao.common.util.JacksonUtils;
import com.wangcaitao.common.util.ResultUtils;
import com.wangcaitao.starter.http.CachedBodyHttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wangcaitao
 */
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Configuration
@WebFilter(filterName = "contentCachingFilter", urlPatterns = "/*")
public class ContentCachingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        // region 校验每页大小
        String pageSizeStr = httpServletRequest.getParameter("pageSize");
        if (StringUtils.isNotBlank(pageSizeStr)) {
            if (Integer.parseInt(pageSizeStr) > PageConstant.MAX_PAGE_SIZE) {
                HttpServletResponseUtils.output(httpServletResponse, JacksonUtils.toJsonString(ResultUtils.error(CommonErrorEnum.PAGE_SIZE_OVER_MAX)));

                return;
            }
        }
        // endregion

        filterChain.doFilter(new CachedBodyHttpServletRequest(httpServletRequest), httpServletResponse);
    }
}
