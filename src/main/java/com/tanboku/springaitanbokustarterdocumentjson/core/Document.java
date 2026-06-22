package com.tanboku.springaitanbokustarterdocumentjson.core;

import java.util.HashMap;
import java.util.Map;

public class Document {
    private final String text;
    private final Map<String, Object> metadata;

    public Document(String text) {
        this(text, new HashMap<>());
    }

    public Document(String text, Map<String, Object> metadata) {
        this.text = text;
        this.metadata = metadata != null ? new HashMap<>(metadata) : new HashMap<>();
    }

    public String getText() {
        return text;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void addMetadata(String key, Object value) {
        this.metadata.put(key, value);
    }
}