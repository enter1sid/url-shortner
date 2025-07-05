package com.urlshortener.model;

import jakarta.persistence.*;

@Entity
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String shortUrl;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String longUrl;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Url() {}
    public Url(String shortUrl, String longUrl, String title) {
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
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
