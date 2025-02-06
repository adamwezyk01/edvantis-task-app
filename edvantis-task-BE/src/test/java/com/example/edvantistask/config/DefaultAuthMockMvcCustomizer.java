package com.example.edvantistask.config;

import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcBuilderCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@TestConfiguration
public class DefaultAuthMockMvcCustomizer {

    @Bean
    public MockMvcBuilderCustomizer mockMvcBuilderCustomizer() {
        return builder -> builder.defaultRequest(get("/").with(
                SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin123")
        ));
    }
}
