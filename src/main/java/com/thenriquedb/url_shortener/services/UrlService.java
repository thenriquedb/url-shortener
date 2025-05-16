package com.thenriquedb.url_shortener.services;

import com.thenriquedb.url_shortener.repositories.UrlRepository;
import com.thenriquedb.url_shortener.schemas.UrlSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class UrlService {
    @Autowired
    UrlRepository urlRepository;

    public UrlSchema getUrlById(String id) {
        return urlRepository.findById(id).orElse(null);
    }

    public String generateShortUrl(String originalUrl, String requestUrl) {
        UrlSchema exitedUrl = urlRepository.findByOriginalUrl(originalUrl);

        if(exitedUrl != null) {
           return requestUrl.replace("shorten", exitedUrl.getId());
        }

        String urlId;
        do {
            urlId = UUID.randomUUID()
                    .toString()
                    .replace("-","")
                    .substring(0, 10);
        } while (urlRepository.existsById(urlId));

        UrlSchema urlSchema = new UrlSchema();
        urlSchema.setId(urlId);
        urlSchema.setOriginalUrl(originalUrl);

        urlRepository.save(urlSchema);

        return requestUrl.replace("shorten", urlId);
    }
}
