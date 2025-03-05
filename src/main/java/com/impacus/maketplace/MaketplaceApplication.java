package com.impacus.maketplace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.impacus.maketplace.repository")
@EnableRedisRepositories(basePackages = "com.impacus.maketplace.redis.repository")
@EnableScheduling
@EnableAsync
public class MaketplaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MaketplaceApplication.class, args);
    }

}
