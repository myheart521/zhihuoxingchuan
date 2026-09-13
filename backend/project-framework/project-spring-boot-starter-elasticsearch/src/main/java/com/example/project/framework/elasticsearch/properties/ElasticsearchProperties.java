package com.example.project.framework.elasticsearch.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "spring.elasticsearch")
public class ElasticsearchProperties {

    /**
     * 集群节点地址列表
     */
    private List<String> uris = new ArrayList<>();

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 连接超时时间
     */
    private Duration connectionTimeout = Duration.ofSeconds(1);

    /**
     * socket 超时时间
     */
    private Duration socketTimeout = Duration.ofSeconds(30);

    /**
     * 请求超时时间
     */
    private Duration requestTimeout = Duration.ofSeconds(5);

    /**
     * 客户端响应缓存大小
     */
    private Integer responseCacheSize = 100;
}
