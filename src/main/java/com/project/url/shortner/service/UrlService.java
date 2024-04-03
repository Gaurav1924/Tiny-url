package com.project.url.shortner.service;

import com.project.url.shortner.dao.UrlStore;
import com.project.url.shortner.repo.UrlStoreDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class UrlService {

    @Autowired
    UrlStoreDao urlStoreDao;

    private static int counter = 0;

    public String generateShortUrl(String longUrl) {
        log.info("Generating short url");
        String hashedValue = DigestUtils.md5Hex(longUrl);
        UrlStore urlStore = fetchUrl(hashedValue);
        if (Objects.isNull(urlStore)) {
            log.info("Returning newly created short url");
            return saveShortUrl(longUrl, hashedValue);
        } else {
            log.info("Returning url from database as same long url already exists");
            return urlStore.getShortUrl();
        }
    }

    private UrlStore fetchUrl(String hashedValue) {
        return urlStoreDao.findByHashValue(hashedValue);
    }

    @Transactional
    private String saveShortUrl(String longUrl, String hashedValue) {
        UrlStore urlStore = buildUrlStore(longUrl, hashedValue);
        UrlStore saved = urlStoreDao.save(urlStore);
        return Optional.ofNullable(saved).map(UrlStore::getShortUrl).orElse(null);
    }

    private UrlStore buildUrlStore(String longUrl, String hashedValue) {
        String shortUrl = buildStoreUrl(hashedValue);
        UrlStore urlStore = new UrlStore();
        urlStore.setShortUrl(shortUrl);
        urlStore.setLongUrl(longUrl);
        urlStore.setHashValue(hashedValue);
        urlStore.setTimestamp(new Date());
        return urlStore;
    }

    private String buildStoreUrl(String hashedValue) {
        hashedValue = StringUtils.join(hashedValue, counter++);
        String encodedUrl = Base64.encodeBase64String(hashedValue.getBytes(StandardCharsets.UTF_8));
        return StringUtils.substring(encodedUrl, 0, 7);
    }

    public String fetchOriginalUrl(String url) {
        log.info("Fetching original url from database");
        UrlStore urlStore = urlStoreDao.findByShortUrl(url);
        if (Objects.nonNull(urlStore)) {
            return urlStore.getLongUrl();
        }
        log.info("Returning null as there is no url present for given short url");
        return null;
    }
}
