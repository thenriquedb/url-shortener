package com.thenriquedb.url_shortener.controllers;

import com.thenriquedb.url_shortener.dtos.UrlRecordDto;
import com.thenriquedb.url_shortener.dtos.UrlResponse;
import com.thenriquedb.url_shortener.schemas.UrlSchema;
import com.thenriquedb.url_shortener.services.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("/")
public class UrlController {
    @Autowired
    UrlService urlService;

    @PostMapping("/shorten")
    public ResponseEntity<UrlResponse> shortenUrl(@RequestBody UrlRecordDto urlRecord, HttpServletRequest request) {
        String shortUrl = urlService.generateShortUrl(urlRecord.url(), request.getRequestURL().toString());
        return ResponseEntity.ok().body(new UrlResponse(shortUrl));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> redirect(@PathVariable String id, HttpServletRequest request) {
        UrlSchema urlSchema = urlService.getUrlById(id);

        if (urlSchema == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Location", urlSchema.getOriginalUrl());

        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
    }
}
