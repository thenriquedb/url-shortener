package com.thenriquedb.url_shortener.repositories;

import com.thenriquedb.url_shortener.schemas.UrlSchema;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface UrlRepository extends MongoRepository<UrlSchema, String> {
    UrlSchema findByOriginalUrl(String originalUrl);
}
