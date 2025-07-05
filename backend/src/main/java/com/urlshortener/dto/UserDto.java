package com.urlshortener.dto;

import java.util.List;

public class UserDto {
    private Long id;
    private String username;
    private String email;
    private List<UrlDto> urls;

    public UserDto() {}
    public UserDto(Long id, String username, String email, List<UrlDto> urls) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.urls = urls;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public List<UrlDto> getUrls() { return urls; }
    public void setUrls(List<UrlDto> urls) { this.urls = urls; }
}
