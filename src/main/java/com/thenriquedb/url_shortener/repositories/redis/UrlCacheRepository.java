package com.thenriquedb.url_shortener.repositories.redis;


import com.thenriquedb.url_shortener.schemas.UrlSchema;
import org.springframework.data.repository.CrudRepository;

public interface UrlCacheRepository extends CrudRepository<UrlSchema, String> {}
