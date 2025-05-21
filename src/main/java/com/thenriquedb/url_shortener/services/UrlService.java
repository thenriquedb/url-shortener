package com.thenriquedb.url_shortener.services;

import com.thenriquedb.url_shortener.repositories.redis.UrlCacheRepository;
import com.thenriquedb.url_shortener.repositories.mongo.UrlRepository;
import com.thenriquedb.url_shortener.schemas.UrlSchema;
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
        Optional<UrlSchema> urlFromCache = urlCacheRepository.findById(urlHash);

        if (urlFromCache.isPresent()) {
            return urlFromCache.get();
        }

        UrlSchema foundedUrl= urlRepository.findById(urlHash).orElse(null);

        if (foundedUrl != null) {
            urlCacheRepository.save(foundedUrl);
        }

        return foundedUrl;
    }

    public String generateShortUrl(String originalUrl, LocalDateTime expireAt, String requestUrl) {
        UrlSchema exitedUrl = urlRepository.findByOriginalUrl(originalUrl);

        if(exitedUrl != null) {
           return requestUrl + "/" + exitedUrl.getId();
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

        urlCacheRepository.save(urlSchema);
        return requestUrl + "/" + urlHash;
    }
}
