package com.impacus.maketplace.config;

import com.google.firebase.FirebaseApp;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestFirebaseConfig {

    @Bean
    @Primary
    public FirebaseApp firebaseApp() {
        // 테스트 환경에서는 FirebaseApp을 초기화하지 않도록 처리
        return null;
    }
}
