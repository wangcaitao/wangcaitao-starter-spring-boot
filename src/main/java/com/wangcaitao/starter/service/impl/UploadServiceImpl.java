package com.wangcaitao.starter.service.impl;

import com.qiniu.util.Etag;
import com.wangcaitao.common.constant.CharConstant;
import com.wangcaitao.common.entity.Result;
import com.wangcaitao.common.exception.ResultException;
import com.wangcaitao.common.util.ResultUtils;
import com.wangcaitao.starter.oss.QiniuOssUtils;
import com.wangcaitao.starter.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangcaitao
 */
@Service
@Slf4j
public class UploadServiceImpl implements UploadService {

    @Override
    public Result<String> upload(MultipartFile file, String prefix) {
        if (null == file) {
            return ResultUtils.error();
        }

        String fileName = file.getOriginalFilename();
        if (StringUtils.isBlank(fileName)) {
            return ResultUtils.error();
        }

        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        byte[] bytes;
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            log.error("上传错误.", e);

            return ResultUtils.error("上传错误");
        }

        return ResultUtils.success(QiniuOssUtils.upload(bytes, handlerPrefix(prefix) + CharConstant.SLASH + Etag.data(bytes) + suffixName));
    }

    @Override
    public Result<String> upload(byte[] bytes, String prefix) {
        return ResultUtils.success(QiniuOssUtils.upload(bytes, handlerPrefix(prefix) + CharConstant.SLASH + Etag.data(bytes)));
    }

    @Override
    public Result<List<String>> upload(MultipartHttpServletRequest request, String prefix) {
        List<MultipartFile> files = request.getFiles("file");
        if (CollectionUtils.isEmpty(files)) {
            return ResultUtils.error();
        }

        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            urls.add(ResultUtils.getData(upload(file, prefix)));
        }

        return ResultUtils.success(urls);
    }

    /**
     * 处理
     *
     * @param prefix prefix
     * @return prefix
     */
    private String handlerPrefix(String prefix) {
        if (StringUtils.isEmpty(prefix)) {
            throw new ResultException("prefix 不能为空");
        }

        if (prefix.startsWith(CharConstant.SLASH)) {
            prefix = prefix.substring(1);
        }

        if (prefix.endsWith(CharConstant.SLASH)) {
            prefix = prefix.substring(0, prefix.length() - 1);
        }

        return prefix;
    }
}
