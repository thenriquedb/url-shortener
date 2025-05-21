package com.thenriquedb.url_shortener.services;

import com.thenriquedb.url_shortener.repositories.redis.UrlCacheRepository;
import com.thenriquedb.url_shortener.repositories.mongo.UrlRepository;
import com.thenriquedb.url_shortener.schemas.UrlCacheSchema;
import com.thenriquedb.url_shortener.schemas.UrlSchema;
import com.thenriquedb.url_shortener.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UrlService {
    @Autowired
    UrlRepository urlRepository;

    @Autowired
    UrlCacheRepository urlCacheRepository;

    public UrlSchema getUrlById(String urlHash) {
        Optional<UrlCacheSchema> urlFromCache = urlCacheRepository.findById(urlHash);

        if (urlFromCache.isPresent()) {
            UrlCacheSchema urlCacheSchema = urlFromCache.get();

            UrlSchema urlSchema = new UrlSchema();
            urlSchema.fromUrlCacheSchema(urlCacheSchema);

            return urlSchema;
        }

        UrlSchema foundedUrl = urlRepository.findById(urlHash).orElse(null);

        if (foundedUrl != null) {
            UrlCacheSchema urlCacheSchema = new UrlCacheSchema();
            urlCacheSchema.fromUrlSchema(foundedUrl);

            urlCacheRepository.save(urlCacheSchema);
        }

        return foundedUrl;
    }

    public String generateShortUrl(String originalUrl, LocalDateTime expireAt, String requestUrl) {
        UrlSchema existedUrl = urlRepository.findByOriginalUrl(originalUrl);

        if(existedUrl != null) {
           return requestUrl + "/" + existedUrl.getId();
        }

        String urlHash;
        do {
            urlHash = UUID.randomUUID()
                    .toString()
                    .replace("-","")
                    .substring(0, 10);
        } while (urlRepository.existsById(urlHash));

        UrlSchema urlSchema = new UrlSchema();
        urlSchema.setId(urlHash);
        urlSchema.setExpiresAt(expireAt);
        urlSchema.setOriginalUrl(originalUrl);

        urlRepository.save(urlSchema);

        UrlCacheSchema urlCacheSchema = new UrlCacheSchema();
        urlCacheSchema.setId(urlHash);
        urlCacheSchema.setOriginalUrl(originalUrl);
        urlCacheSchema.setExpirationInSeconds(DateUtils.calculateDifferenceInSeconds(expireAt));

        urlCacheRepository.save(urlCacheSchema);

        return requestUrl + "/" + urlHash;
    }
}
