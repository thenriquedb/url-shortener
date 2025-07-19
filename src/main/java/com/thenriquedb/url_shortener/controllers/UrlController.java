package com.thenriquedb.url_shortener.controllers;

import com.thenriquedb.url_shortener.dtos.UrlRecordDto;
import com.thenriquedb.url_shortener.dtos.UrlResponseRecordDto;
import com.thenriquedb.url_shortener.shared.schemas.UrlSchema;
import com.thenriquedb.url_shortener.services.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/")
public class UrlController {
    @Autowired
    UrlService urlService;

    @PostMapping("/shorten")
    public ResponseEntity<UrlResponseRecordDto> shortenUrl(@RequestBody UrlRecordDto urlRecord, HttpServletRequest request,@RequestHeader(value = "Forwarded", required = false) String forwardedHeader, @RequestHeader Map<String, String> headers) {
        LocalDateTime expiresAt = null;

        if (urlRecord.expiresAt() != null) {
            expiresAt = LocalDateTime.parse(urlRecord.expiresAt());
        }

        String forwardedHost = request.getHeader("host");
        String requestProtocol = request.getScheme();

        String requestUrl = "";
        if(forwardedHost != null) {
            requestUrl += requestProtocol + "://" + forwardedHost;
        } else {
            requestUrl += request.getRequestURL().toString().replace(request.getRequestURI(), "");
        }

        String shortUrl = urlService.generateShortUrl(urlRecord.url(), expiresAt, requestUrl);

        return ResponseEntity.ok().body(new UrlResponseRecordDto(shortUrl, expiresAt));
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
