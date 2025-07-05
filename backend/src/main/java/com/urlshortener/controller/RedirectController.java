package com.urlshortener.controller;

import com.urlshortener.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class RedirectController {
    @Autowired
    private UrlService urlService;

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Object> redirect(@PathVariable String shortUrl) {
        return urlService.getByShortUrl(shortUrl)
                .map(url -> ResponseEntity.status(HttpStatus.FOUND)
                        .location(URI.create(url.getLongUrl()))
                        .build())
                .orElse(ResponseEntity.notFound().build());
    }
}
