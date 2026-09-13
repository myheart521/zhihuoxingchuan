package com.example.project.module.infra.framework.file.core.client.s3;

import cn.hutool.core.util.StrUtil;
import com.example.project.module.infra.framework.file.core.client.FileClientConfig;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

/**
 * S3 文件客户端的配置类
 *

 */
@Data
public class S3FileClientConfig implements FileClientConfig {

    public static final String ENDPOINT_QINIU = "qiniucs.com";
    public static final String ENDPOINT_ALIYUN = "aliyuncs.com";
    public static final String ENDPOINT_TENCENT = "myqcloud.com";
    public static final String ENDPOINT_VOLCES = "volces.com"; // 火山云（字节）

    /**
     * 节点地址
     * 1. MinIO：https://www.iocoder.cn/Spring-Boot/MinIO 。例如说，http://127.0.0.1:9000
     * 2. 阿里云：https://example.invalid/resource
     * 3. 腾讯云：https://example.invalid/resource
     * 4. 七牛云：https://example.invalid/resource
     * 5. 华为云：https://example.invalid/resource
     * 6. 火山云：https://example.invalid/resource
     */
    @NotNull(message = "endpoint 不能为空")
    private String endpoint;
    /**
     * 自定义域名
     * 1. MinIO：通过 Nginx 配置
     * 2. 阿里云：https://example.invalid/resource
     * 3. 腾讯云：https://example.invalid/resource
     * 4. 七牛云：https://example.invalid/resource
     * 5. 华为云：https://example.invalid/resource
     * 6. 火山云：https://example.invalid/resource
     */
    @URL(message = "domain 必须是 URL 格式")
    private String domain;
    /**
     * 存储 Bucket
     */
    @NotNull(message = "bucket 不能为空")
    private String bucket;

    /**
     * 访问 Key
     * 1. MinIO：https://www.iocoder.cn/Spring-Boot/MinIO
     * 2. 阿里云：https://example.invalid/resource
     * 3. 腾讯云：https://example.invalid/resource
     * 4. 七牛云：https://example.invalid/resource
     * 5. 华为云：https://example.invalid/resource
     * 6. 火山云：https://example.invalid/resource
     */
    @NotNull(message = "accessKey 不能为空")
    private String accessKey;
    /**
     * 访问 Secret
     */
    @NotNull(message = "accessSecret 不能为空")
    private String accessSecret;

    @SuppressWarnings("RedundantIfStatement")
    @AssertTrue(message = "domain 不能为空")
    @JsonIgnore
    public boolean isDomainValid() {
        // 如果是七牛，必须带有 domain
        if (StrUtil.contains(endpoint, ENDPOINT_QINIU) && StrUtil.isEmpty(domain)) {
            return false;
        }
        return true;
    }

}
