package org.example.records;


public record UrlAnalyticsResponse(Long clickCount, String lastAccessedAt, String createdAt,
            String longUrl) {}
