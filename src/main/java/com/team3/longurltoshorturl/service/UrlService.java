package com.team3.longurltoshorturl.service;

import com.team3.longurltoshorturl.dto.UrlLongRequest;
import com.team3.longurltoshorturl.entity.Url;
import com.team3.longurltoshorturl.repository.Impl.UrlRepositoryImpl;
import com.relops.snowflake.Snowflake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

@Service
public class UrlService {
    @Autowired
    private final UrlRepositoryImpl urlRepository;
    @Autowired
    private final BaseConversion conversion;

    private final int id = 1;

    public UrlService(UrlRepositoryImpl urlRepository, BaseConversion conversion) {
        this.urlRepository = urlRepository;
        this.conversion = conversion;
    }

    public String convertToShortUrl(UrlLongRequest request) {
        Snowflake s = new Snowflake(id);
        Url url = new Url();
        url.setLongUrl(request.getLongUrl());
        url.setExpiresDate(request.getExpiresDate());
        url.setCreatedDate(new Date());
        url.setId(s.next());
        Url entity = null;
        try {
            entity = urlRepository.save(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return conversion.encode(entity.getId());
    }

    public String getOriginalUrl(String shortUrl) {
        long id = conversion.decode(shortUrl);
        Url entity = null;
        try {
            entity = urlRepository.findById(id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (entity.getExpiresDate() != null && entity.getExpiresDate().before(new Date())){
            urlRepository.delete(entity);
        }

        return entity.getLongUrl();
    }
}
