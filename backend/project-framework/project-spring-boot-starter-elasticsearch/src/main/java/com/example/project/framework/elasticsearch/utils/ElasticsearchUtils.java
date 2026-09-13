package com.example.project.framework.elasticsearch.utils;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ElasticsearchUtils {

    private final ElasticsearchClient client;

    /**
     * 创建索引
     */
    @SneakyThrows
    public boolean createIndex(String indexName) {
        return client.indices().create(c -> c.index(indexName)).acknowledged();
    }

    /**
     * 删除索引
     */
    @SneakyThrows
    public boolean deleteIndex(String indexName) {
        return client.indices().delete(c -> c.index(indexName)).acknowledged();
    }

    /**
     * 判断索引是否存在
     */
    @SneakyThrows
    public boolean existsIndex(String indexName) {
        return client.indices().exists(c -> c.index(indexName)).value();
    }

    /**
     * 添加文档
     */
    @SneakyThrows
    public <T> String addDocument(String indexName, T document) {
        IndexResponse response = client.index(i -> i
                .index(indexName)
                .document(document)
        );
        return response.id();
    }

    /**
     * 批量添加文档
     */
    @SneakyThrows
    public <T> void bulkAddDocument(String indexName, List<T> documents) {
        BulkRequest.Builder br = new BulkRequest.Builder();
        for (T document : documents) {
            br.operations(op -> op
                    .index(idx -> idx
                            .index(indexName)
                            .document(document)
                    )
            );
        }
        client.bulk(br.build());
    }

    /**
     * 根据ID获取文档
     */
    @SneakyThrows
    public <T> T getDocument(String indexName, String id, Class<T> clazz) {
        GetResponse<T> response = client.get(g -> g
                .index(indexName)
                .id(id),
                clazz
        );
        return response.source();
    }

    /**
     * 搜索文档
     */
    @SneakyThrows
    public <T> List<T> search(String indexName, String fieldName, String keyword, Class<T> clazz) {
        SearchResponse<T> response = client.search(s -> s
                .index(indexName)
                .query(q -> q
                        .match(m -> m
                                .field(fieldName)
                                .query(keyword)
                        )
                ),
                clazz
        );

        return response.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }

    /**
     * 更新文档
     */
    @SneakyThrows
    public <T> void updateDocument(String indexName, String id, T document) {
        client.update(u -> u
                .index(indexName)
                .id(id)
                .doc(document),
                Object.class
        );
    }

    /**
     * 删除文档
     */
    @SneakyThrows
    public void deleteDocument(String indexName, String id) {
        client.delete(d -> d
                .index(indexName)
                .id(id)
        );
    }
}
