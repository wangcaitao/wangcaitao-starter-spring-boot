package com.wangcaitao.starter.http;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * cached response body
 *
 * @author wangcaitao
 */
public class CachedBodyHttpServletResponse extends HttpServletResponseWrapper {

    /**
     * 缓存数据
     */
    private ByteArrayOutputStream cachedOutputStream = new ByteArrayOutputStream();

    /**
     * response
     */
    private HttpServletResponse response;

    /**
     * Constructor
     *
     * @param response response
     */
    public CachedBodyHttpServletResponse(HttpServletResponse response) {
        super(response);
        this.response = response;
    }

    /**
     * 获取 body
     *
     * @return byte[]
     */
    public byte[] getBody() {
        return cachedOutputStream.toByteArray();
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {

            }

            @Override
            public void write(int b) {
                cachedOutputStream.write(b);
            }

            @Override
            public void flush() throws IOException {
                if (!response.isCommitted()) {
                    byte[] body = getBody();
                    ServletOutputStream outputStream = response.getOutputStream();
                    outputStream.write(body);
                    outputStream.flush();
                }
            }
        };
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(new OutputStreamWriter(cachedOutputStream, response.getCharacterEncoding()));
    }
}
