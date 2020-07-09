package com.wangcaitao.starter.service;

import com.wangcaitao.common.entity.Result;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

/**
 * @author wangcaitao
 */
public interface UploadService {

    /**
     * 单文件上传
     *
     * @param file   file
     * @param prefix prefix
     * @return Result
     */
    Result<String> upload(MultipartFile file, String prefix);

    /**
     * 文件字节上传
     *
     * @param bytes  bytes
     * @param prefix prefix
     * @return Result
     */
    Result<String> upload(byte[] bytes, String prefix);

    /**
     * 批量上传
     *
     * @param request request
     * @param prefix  prefix
     * @return Result
     */
    Result<List<String>> upload(MultipartHttpServletRequest request, String prefix);
}
