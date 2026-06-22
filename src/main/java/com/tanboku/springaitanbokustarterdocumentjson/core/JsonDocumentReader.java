package com.tanboku.springaitanbokustarterdocumentjson.core;

import com.example.reader.core.Document;
import com.example.reader.core.DocumentReader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JsonDocumentReader implements DocumentReader {

    private final Resource resource;
    private final String[] jsonKeysToUse;
    private final JsonMetadataGenerator metadataGenerator;
    private final ObjectMapper objectMapper;

    /**
     * 构造函数 - 仅资源路径
     */
    public JsonDocumentReader(Resource resource) {
        this(resource, new String[]{}, null);
    }

    /**
     * 构造函数 - 指定提取内容的 key
     */
    public JsonDocumentReader(Resource resource, String... jsonKeysToUse) {
        this(resource, jsonKeysToUse, null);
    }

    /**
     * 构造函数 - 完整配置
     */
    public JsonDocumentReader(Resource resource, String[] jsonKeysToUse, 
                               JsonMetadataGenerator metadataGenerator) {
        this.resource = resource;
        this.jsonKeysToUse = jsonKeysToUse != null ? jsonKeysToUse : new String[]{};
        this.metadataGenerator = metadataGenerator;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public List<Document> get() {
        try (InputStream inputStream = resource.getInputStream()) {
            JsonNode rootNode = objectMapper.readTree(inputStream);
            return parseJsonNode(rootNode);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON from resource: " + resource, e);
        }
    }

    private List<Document> parseJsonNode(JsonNode node) {
        List<Document> documents = new ArrayList<>();

        if (node.isArray()) {
            for (JsonNode item : node) {
                documents.add(createDocumentFromNode(item));
            }
        } else if (node.isObject()) {
            documents.add(createDocumentFromNode(node));
        } else {
            // 如果是原始值，直接作为文本
            documents.add(new Document(node.asText()));
        }

        return documents;
    }

    private Document createDocumentFromNode(JsonNode node) {
        String content = extractContent(node);
        Document document = new Document(content);

        // 生成元数据
        if (metadataGenerator != null) {
            metadataGenerator.generateMetadata(document, node);
        }

        // 添加默认元数据
        document.addMetadata("source", resource.getDescription());

        return document;
    }

    private String extractContent(JsonNode node) {
        if (jsonKeysToUse.length == 0) {
            // 未指定 key，使用整个 JSON 作为内容
            return node.toString();
        }

        // 按指定 key 提取内容
        StringBuilder content = new StringBuilder();
        for (String key : jsonKeysToUse) {
            JsonNode valueNode = node.get(key);
            if (valueNode != null && !valueNode.isNull()) {
                if (content.length() > 0) {
                    content.append(" ");
                }
                content.append(valueNode.asText());
            }
        }
        return content.toString();
    }
}