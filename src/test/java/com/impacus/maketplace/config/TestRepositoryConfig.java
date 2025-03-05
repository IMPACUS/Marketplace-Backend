package com.impacus.maketplace.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import({QuerydslConfig.class, EmptyAuditingTestConfig.class, RedisConfig.class, RedisTestConfig.class})
public class TestRepositoryConfig {
}
