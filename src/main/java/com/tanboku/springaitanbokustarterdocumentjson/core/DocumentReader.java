package com.tanboku.springaitanbokustarterdocumentjson.core;

import java.util.List;
import java.util.function.Supplier;

public interface DocumentReader extends Supplier<List<Document>> {
    default List<Document> read() {
        return get();
    }
}