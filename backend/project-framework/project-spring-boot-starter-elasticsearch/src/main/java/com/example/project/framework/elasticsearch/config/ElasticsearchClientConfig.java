package com.example.project.framework.elasticsearch.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.example.project.framework.elasticsearch.properties.ElasticsearchProperties;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableConfigurationProperties(ElasticsearchProperties.class)
public class ElasticsearchClientConfig {

    @Bean
    @ConditionalOnMissingBean
    public ElasticsearchClient elasticsearchClient(ElasticsearchProperties properties) {
        // 转换节点地址为HttpHost数组
        List<HttpHost> httpHosts = properties.getUris().stream()
                .map(HttpHost::create)
                .collect(Collectors.toList());

        // 构建RestClient
        RestClientBuilder builder = RestClient.builder(httpHosts.toArray(new HttpHost[0]));

        // 设置超时时间
        builder.setRequestConfigCallback(requestConfigBuilder -> requestConfigBuilder
                .setConnectTimeout(Math.toIntExact(properties.getConnectionTimeout().toMillis()))
                .setSocketTimeout(Math.toIntExact(properties.getSocketTimeout().toMillis())));

        // 设置认证信息
        if (StringUtils.hasText(properties.getUsername())) {
            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(properties.getUsername(), properties.getPassword()));

            builder.setHttpClientConfigCallback(httpClientBuilder ->
                    httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
        }

        // 创建传输层
        ElasticsearchTransport transport = new RestClientTransport(
                builder.build(), new JacksonJsonpMapper());

        // 创建客户端
        return new ElasticsearchClient(transport);
    }
}
