package com.urlshortener.dto;

public class UrlDto {
    private Long id;
    private String shortUrl;
    private String longUrl;
    private String title;

    public UrlDto() {}
    public UrlDto(Long id, String shortUrl, String longUrl, String title) {
        this.id = id;
        this.shortUrl = shortUrl;
        this.longUrl = longUrl;
        this.title = title;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getShortUrl() { return shortUrl; }
    public void setShortUrl(String shortUrl) { this.shortUrl = shortUrl; }
    public String getLongUrl() { return longUrl; }
    public void setLongUrl(String longUrl) { this.longUrl = longUrl; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
}
