package com.tanboku.springaitanbokustarterdocumentjson.core;

import com.example.reader.core.Document;
import com.example.reader.json.JsonDocumentReader;
import org.springframework.core.io.ClassPathResource;

import java.util.List;

public class JsonReaderExample {
    public static void main(String[] args) {
        // 方式1：读取整个 JSON
        JsonDocumentReader reader = new JsonDocumentReader(
            new ClassPathResource("data.json")
        );
        List<Document> documents = reader.get();

        // 方式2：指定提取的 key
        JsonDocumentReader readerWithKeys = new JsonDocumentReader(
            new ClassPathResource("bikes.json"),
            "description", "content"
        );
        List<Document> docs = readerWithKeys.get();

        // 方式3：带自定义元数据生成器
        JsonDocumentReader readerWithMetadata = new JsonDocumentReader(
            new ClassPathResource("data.json"),
            new String[]{"title", "body"},
            (doc, node) -> {
                doc.addMetadata("id", node.get("id").asText());
                doc.addMetadata("timestamp", node.get("created_at").asText());
            }
        );
        List<Document> docsWithMeta = readerWithMetadata.get();

        for (Document doc : docsWithMeta) {
            System.out.println("Content: " + doc.getText());
            System.out.println("Metadata: " + doc.getMetadata());
        }
    }
}