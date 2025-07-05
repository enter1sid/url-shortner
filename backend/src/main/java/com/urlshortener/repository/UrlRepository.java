package com.urlshortener.repository;

import com.urlshortener.model.Url;
import com.urlshortener.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> findByShortUrl(String shortUrl);
    List<Url> findAllByUser(User user);
}
