package com.cqupt.urbansense.config;

import com.cqupt.urbansense.filter.AuthorizeFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.WebFilter;

@Configuration
public class WebFluxConfig {

    @Bean
    public WebFilter authorizeFilter() {
        return new AuthorizeFilter();
    }
}
