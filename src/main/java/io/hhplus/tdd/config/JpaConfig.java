package io.hhplus.tdd.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.TimeZone;

@Configuration
@EnableJpaAuditing
public class JpaConfig {

    @PostConstruct
    public void setTime() {
        System.out.println("JpaConfig!!!!!!!!!!!!!!");
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }
}