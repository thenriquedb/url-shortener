package com.thenriquedb.url_shortener.schemas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "url")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UrlSchema {
    @MongoId
    String id;
    String originalUrl;

    public UrlSchema(String originalUrl) {
        this.originalUrl = originalUrl;
    }
}
