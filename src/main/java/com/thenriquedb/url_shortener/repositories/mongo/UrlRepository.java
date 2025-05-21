package com.thenriquedb.url_shortener.repositories.mongo;

import com.thenriquedb.url_shortener.shared.schemas.UrlSchema;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UrlRepository extends MongoRepository<UrlSchema, String> {
    UrlSchema findByOriginalUrl(String originalUrl);
}
