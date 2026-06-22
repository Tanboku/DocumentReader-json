package com.tanboku.springaitanbokustarterdocumentjson.core;

import com.example.reader.core.Document;
import com.fasterxml.jackson.databind.JsonNode;

@FunctionalInterface
public interface JsonMetadataGenerator {
    void generateMetadata(Document document, JsonNode jsonNode);
}