package org.example.entity;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "ShortenedUrls")
public class ShortenedUrlEntity {

    @DynamoDBHashKey(attributeName = "shortUrl")
    private String shortUrl;

    @DynamoDBAttribute(attributeName = "longUrl")
    private String longUrl;

    @DynamoDBAttribute(attributeName = "clickCount")
    private Long clickCount;

    @DynamoDBAttribute(attributeName = "lastAccessedAt")
    private String lastAccessedAt;

    @DynamoDBAttribute(attributeName = "createdAt")
    private String createdAt;

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public Long getClickCount() {
        return clickCount;
    }

    public void setClickCount(Long clickCount) {
        this.clickCount = clickCount;
    }

    public String getLastAccessedAt() {
        return lastAccessedAt;
    }

    public void setLastAccessedAt(String lastAccessedAt) {
        this.lastAccessedAt = lastAccessedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public ShortenedUrlEntity() {}

    public ShortenedUrlEntity(String shortUrl, String longUrl, Long clickCount, String lastAccessedAt, String createdAt) {
        this.shortUrl = shortUrl;
        this.longUrl = longUrl;
        this.clickCount = clickCount;
        this.lastAccessedAt = lastAccessedAt;
        this.createdAt = createdAt;
    }
}
