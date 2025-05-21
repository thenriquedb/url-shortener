package com.thenriquedb.url_shortener.shared.schemas;

import com.thenriquedb.url_shortener.shared.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("url")
public class UrlCacheSchema  {
    String id;

    String originalUrl;

    @TimeToLive
    private Long expirationInSeconds;

    public void fromUrlSchema(UrlSchema urlSchema) {
        this.id = urlSchema.getId();
        this.originalUrl = urlSchema.getOriginalUrl();
        this.expirationInSeconds = DateUtils.calculateDifferenceInSeconds(urlSchema.getExpiresAt());
    }
}
