package com.team3.longurltoshorturl;


import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableCaching
public class UrlShortenerApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(UrlShortenerApiApplication.class, args);

    }

    @Bean
    public OpenApiCustomiser serverOpenApiCustomiser1() {
        return openAPI -> openAPI.getServers().forEach(server -> server.setDescription("my new description"));
    }
}
