package com.thenriquedb.url_shortener.services;

import com.thenriquedb.url_shortener.repositories.UrlRepository;
import com.thenriquedb.url_shortener.schemas.UrlSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UrlService {
    @Autowired
    UrlRepository urlRepository;

    public UrlSchema getUrlById(String id) {
        return urlRepository.findById(id).orElse(null);
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

        return requestUrl + "/" + urlHash;
    }
}
