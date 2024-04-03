package com.project.url.shortner.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "url_store")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UrlStore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "long_url")
    private String longUrl;

    @Column(name = "short_url")
    private String shortUrl;

    @Column(name = "hash_value")
    private String hashValue;

    private Date timestamp;
}
