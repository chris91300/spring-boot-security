package com.simplon.tests.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${frontend.angular.url}")
    private String frontendAngularUrl;

    @Value("${frontend.react.url}")
    private String frontendReactUrl;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(this.frontendAngularUrl, this.frontendReactUrl)
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("Authorization");
    }
}
