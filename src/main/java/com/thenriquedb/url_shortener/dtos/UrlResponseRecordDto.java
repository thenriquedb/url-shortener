package com.thenriquedb.url_shortener.dtos;

import java.time.LocalDateTime;

public record UrlResponseRecordDto(String shortUrl, LocalDateTime expiresAt) {
}
