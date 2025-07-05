package com.urlshortener.service;

import com.urlshortener.dto.UrlDto;
import com.urlshortener.model.Url;
import com.urlshortener.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UrlService {
    @Autowired
    private UrlRepository urlRepository;

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SHORT_URL_LENGTH = 7;
    private final SecureRandom random = new SecureRandom();

    public Url createShortUrl(String longUrl, String title, com.urlshortener.model.User user) {
        String shortUrl;
        do {
            shortUrl = generateShortUrl();
        } while (urlRepository.findByShortUrl(shortUrl).isPresent());
        Url url = new Url(shortUrl, longUrl, title);
        url.setUser(user);
        return urlRepository.save(url);
    }

    public Optional<Url> getByShortUrl(String shortUrl) {
        return urlRepository.findByShortUrl(shortUrl);
    }

    public Iterable<Url> getAllUrls() {
        return urlRepository.findAll();
    }

    public Iterable<Url> getAllUrlsByUser(com.urlshortener.model.User user) {
        return urlRepository.findAll().stream().filter(url -> url.getUser() != null && url.getUser().getId().equals(user.getId())).toList();
    }

    public List<UrlDto> getAllUrlDtosByUser(com.urlshortener.model.User user) {
        List<Url> urls = new ArrayList<>();
        getAllUrlsByUser(user).forEach(urls::add);
        return urls.stream().map(this::toDto).collect(Collectors.toList());
    }

    public UrlDto toDto(Url url) {
        return new UrlDto(url.getId(), url.getShortUrl(), url.getLongUrl(), url.getTitle());
    }

    private String generateShortUrl() {
        StringBuilder sb = new StringBuilder(SHORT_URL_LENGTH);
        for (int i = 0; i < SHORT_URL_LENGTH; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    public boolean deleteUrlByIdAndUser(Long id, com.urlshortener.model.User user) {
        Optional<Url> urlOpt = urlRepository.findById(id);
        if (urlOpt.isPresent()) {
            Url url = urlOpt.get();
            if (url.getUser() != null && url.getUser().getId().equals(user.getId())) {
                urlRepository.deleteById(id);
                return true;
            }
        }
        return false;
    }
}
