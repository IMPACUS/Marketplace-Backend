//package com.impacus.maketplace.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
//import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
//import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
//import org.springframework.security.oauth2.client.registration.ClientRegistration;
//import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
//import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
//import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
//import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
//import org.springframework.security.oauth2.core.AuthorizationGrantType;
//import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
//
//@Configuration
//@EnableOAuth2Client
//public class OAuth2ClientConfig {
//
//    @Value("${spring.security.oauth2.client.registration.apple.clientId}")
//    private String appleClientId;
//    @Value("${apple.key}")
//    private String clientSecret;
//    @Value("${spring.security.oauth2.client.registration.apple.redirectUri}")
//    private String redirectUri;
//
//    @Bean
//    public OAuth2AuthorizedClientManager authorizedClientManager(
//            ClientRegistrationRepository clientRegistrationRepository,
//            OAuth2AuthorizedClientRepository authorizedClientRepository) {
//
//        OAuth2AuthorizedClientProvider authorizedClientProvider =
//                OAuth2AuthorizedClientProviderBuilder.builder()
//                        .authorizationCode()
//                        .refreshToken()
//                        .build();
//
//        DefaultOAuth2AuthorizedClientManager authorizedClientManager =
//                new DefaultOAuth2AuthorizedClientManager(
//                        clientRegistrationRepository, authorizedClientRepository);
//
//        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
//
//        return authorizedClientManager;
//    }
//
//    @Bean
//    public ClientRegistrationRepository clientRegistrationRepository() {
//        return new InMemoryClientRegistrationRepository(this.appleClientRegistration());
//    }
//
//    private ClientRegistration appleClientRegistration() {
//        return ClientRegistration.withRegistrationId("apple")
//                .clientId(this.appleClientId)
//                .clientSecret(this.clientSecret)
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .redirectUri(this.redirectUri)
//                .scope("email")
//                .authorizationUri("https://appleid.apple.com/auth/authorize")
//                .tokenUri("https://appleid.apple.com/auth/token")
//                .userInfoUri("https://appleid.apple.com/auth/userinfo")
//                .userNameAttributeName("sub")
//                .jwkSetUri("https://appleid.apple.com")
//                .build();
//    }
//}