package com.wangcaitao.starter.oss;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import com.wangcaitao.common.constant.CharConstant;
import com.wangcaitao.common.exception.ResultException;
import com.wangcaitao.common.util.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 七牛云对象存储服务工具
 *
 * @author wangcaitao
 */
@Slf4j
public class QiniuOssUtils {

    /**
     * accessKey
     */
    public static String ACCESS_KEY;

    /**
     * secretKey
     */
    public static String SECRET_KEY;

    /**
     * bucket
     */
    public static String BUCKET;

    /**
     * 基础路径
     */
    public static String BASE_URL;

    /**
     * key 基础前缀
     */
    public static String BASE_KEY;

    /**
     * 上传文件
     *
     * @param data data
     * @param key  key
     * @return true or false
     */
    public static String upload(byte[] data, String key) {
        String url = BASE_URL + CharConstant.SLASH + key;

        Configuration configuration = new Configuration();
        UploadManager uploadManager = new UploadManager(configuration);

        try {
            key = StringUtils.isEmpty(BASE_KEY) ? key : BASE_KEY + CharConstant.SLASH + key;
            Response response = uploadManager.put(data, key, getToken());
            if (!response.isOK()) {
                log.error("上传文件失败. key: {}, result: {}", key, JacksonUtils.toJsonString(response));
            }
        } catch (QiniuException e) {
            log.error("上传文件失败, key: {}", key, e);

            throw new ResultException("上传失败");
        }

        return url;
    }

    /**
     * 判断是否存在
     *
     * @param key key
     * @return true or false
     */
    public static boolean isExist(String key) {
        boolean flag = false;

        Configuration configuration = new Configuration();
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        BucketManager bucketManager = new BucketManager(auth, configuration);

        try {
            key = StringUtils.isEmpty(BASE_KEY) ? key : BASE_KEY + CharConstant.SLASH + key;
            FileInfo fileInfo = bucketManager.stat(BUCKET, key);
            if (null != fileInfo) {
                flag = true;
            }
        } catch (QiniuException e) {
            log.error("error. key: {}", key, e);
        }

        return flag;
    }

    /**
     * 删除
     *
     * @param key key
     * @return 是否删除成功. true: 成功, false: 失败
     */
    public static boolean delete(String key) {
        boolean flag = true;

        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        Configuration configuration = new Configuration();
        BucketManager bucketManager = new BucketManager(auth, configuration);
        try {
            key = StringUtils.isEmpty(BASE_KEY) ? key : BASE_KEY + CharConstant.SLASH + key;
            bucketManager.delete(BUCKET, key);
        } catch (QiniuException e) {
            log.error("文件删除失败. key: {}", key, e);

            flag = false;
        }

        return flag;
    }

    /**
     * 获取 token
     *
     * @return token
     */
    private static String getToken() {
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

        String token = auth.uploadToken(BUCKET);
        if (StringUtils.isBlank(token)) {
            throw new RuntimeException();
        }

        return token;
    }
}
