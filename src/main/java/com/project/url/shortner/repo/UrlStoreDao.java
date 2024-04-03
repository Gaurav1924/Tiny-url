package com.project.url.shortner.repo;

import com.project.url.shortner.dao.UrlStore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlStoreDao extends JpaRepository<UrlStore,Long> {
    UrlStore findByShortUrl(String url);

    UrlStore findByHashValue(String hashedValue);
}
