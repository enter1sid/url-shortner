package com.urlshortener.controller;

import com.urlshortener.model.Url;
import com.urlshortener.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.urlshortener.model.User;
import com.urlshortener.dto.UrlDto;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/url")
public class UrlController {
    @Autowired
    private UrlService urlService;

    @PostMapping("/shorten")
    public ResponseEntity<UrlDto> createShortUrl(@RequestBody Map<String, String> request, @AuthenticationPrincipal User user) {
        String longUrl = request.get("longUrl");
        String title = request.get("title");
        Url url = urlService.createShortUrl(longUrl, title, user);
        UrlDto urlDto = urlService.toDto(url);
        return ResponseEntity.ok(urlDto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UrlDto>> getAllUrls(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(urlService.getAllUrlDtosByUser(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUrl(@PathVariable Long id, @AuthenticationPrincipal User user) {
        boolean deleted = urlService.deleteUrlByIdAndUser(id, user);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(403).body("Unauthorized or URL not found");
        }
    }
}