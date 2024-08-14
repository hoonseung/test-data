package com.backend.testdata.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class JpaConfiguration {


    @Bean
    public AuditorAware<String> auditorAware(){
        return () -> Optional.ofNullable("hoonseung");

    }
}
