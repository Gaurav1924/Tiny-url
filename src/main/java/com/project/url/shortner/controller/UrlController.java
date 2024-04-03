package com.project.url.shortner.controller;

import com.project.url.shortner.dto.RequestUrl;
import com.project.url.shortner.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UrlController {

    @Autowired
    UrlService urlService;

    @Value("${url.address}")
    private String urlAddress;

    @PostMapping("/generateTinyUrl")
    String createTinyUrl(@RequestBody RequestUrl requestUrl) {
        String longUrl = requestUrl.getLongUrl();
        String shortUrl = urlService.generateShortUrl(longUrl);
        return urlAddress+"api/redirect/" + shortUrl;
    }

    @GetMapping("/fetchOriginalUrl/{url}")
    String getOriginalUrl(@PathVariable String url){
        return urlService.fetchOriginalUrl(url);
    }

    @GetMapping(value = "/redirect/{url}")
    public RedirectView redirect(@RequestParam Map<String,String> input,@PathVariable String url){
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(urlService.fetchOriginalUrl(url));
        return redirectView;

    }
}
