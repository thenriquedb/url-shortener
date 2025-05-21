package com.thenriquedb.url_shortener.shared.schemas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Document(collection = "url")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("url")
public class UrlSchema implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @MongoId
    String id;
    String originalUrl;

    @Indexed(name = "ttl", expireAfter = "0")
    LocalDateTime expiresAt;

    public void fromUrlCacheSchema(UrlCacheSchema urlCacheSchema) {
        this.id = urlCacheSchema.getId();
        this.originalUrl = urlCacheSchema.getOriginalUrl();
        this.expiresAt = LocalDateTime.now().plusSeconds(urlCacheSchema.getExpirationInSeconds());
    }
}
