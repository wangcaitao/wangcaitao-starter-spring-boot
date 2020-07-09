package com.wangcaitao.starter.http;

import com.wangcaitao.common.util.InputStreamUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * cached body<br>
 * see <p>https://www.baeldung.com/spring-reading-httpservletrequest-multiple-times</p>
 *
 * @author wangcaitao
 */
@Slf4j
public class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {

    /**
     * 缓存数据
     */
    private byte[] cachedBody;

    /**
     * Constructor
     *
     * @param request request
     * @throws IOException IOException
     */
    public CachedBodyHttpServletRequest(HttpServletRequest request) throws IOException {
        super(request);
        this.cachedBody = InputStreamUtils.getByte(request.getInputStream());
    }

    @Override
    public ServletInputStream getInputStream() {
        InputStream cachedBodyInputStream = new ByteArrayInputStream(cachedBody);
        return new ServletInputStream() {

            @Override
            public boolean isFinished() {
                try {
                    return cachedBodyInputStream.available() == 0;
                } catch (IOException e) {
                    log.error("io exception.", e);
                }

                return false;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return cachedBodyInputStream.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(this.cachedBody)));
    }
}
