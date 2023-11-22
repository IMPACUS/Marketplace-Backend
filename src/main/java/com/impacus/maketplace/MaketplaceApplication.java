package com.impacus.maketplace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.impacus.maketplace.repository")
@EnableRedisRepositories(basePackages = "com.impacus.maketplace.redis.repository")
public class MaketplaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MaketplaceApplication.class, args);
    }

}
