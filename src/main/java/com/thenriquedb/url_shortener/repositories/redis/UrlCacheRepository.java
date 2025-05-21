package com.thenriquedb.url_shortener.repositories.redis;


import com.thenriquedb.url_shortener.shared.schemas.UrlCacheSchema;
import org.springframework.data.repository.CrudRepository;

public interface UrlCacheRepository extends CrudRepository<UrlCacheSchema, String> {}
