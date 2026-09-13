package com.example.project.framework.elasticsearch.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Import;
import co.elastic.clients.elasticsearch.ElasticsearchClient;

@AutoConfiguration
@ConditionalOnClass(ElasticsearchClient.class)
@Import(ElasticsearchClientConfig.class)
public class ElasticsearchAutoConfiguration {

    // 这里可以添加其他自定义的Bean
}
