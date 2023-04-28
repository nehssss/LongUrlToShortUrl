package com.team3.longurltoshorturl.repository;

import com.team3.longurltoshorturl.entity.Url;

import java.io.IOException;


public interface
UrlRepository{
    public Url save(Url url) throws IOException;
    public Url findById(long id) throws IOException;

    public void delete(Url url);
}
