package com.team3.longurltoshorturl.controller;

import com.team3.longurltoshorturl.dto.UrlLongRequest;
import com.team3.longurltoshorturl.service.UrlService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/")
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    //@ApiOperation(value = "Convert new url", notes = "Converts long url to short url")
    @PostMapping("create-short")
    @CrossOrigin
    public String convertToShortUrl(@RequestBody UrlLongRequest request) {
        return urlService.convertToShortUrl(request);
    }

    //@ApiOperation(value = "Redirect", notes = "Finds original url from short url and redirects")
    @GetMapping(value = "{shortUrl}")
    @Cacheable(value = "urls", key = "#shortUrl", sync = true)
    public ResponseEntity<Void> getAndRedirect(@PathVariable String shortUrl) {
        String url = urlService.getOriginalUrl(shortUrl);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(url))
                .build();
    }

    @GetMapping("hello")
    public String heel() {
        return "Hello word";
    }
}
