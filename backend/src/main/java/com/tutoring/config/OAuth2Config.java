package com.tutoring.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class OAuth2Config {

    @Value("${spring.security.oauth2.client.registration.google.client-id:}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret:}")
    private String googleClientSecret;

    @Value("${spring.security.oauth2.client.registration.github.client-id:}")
    private String githubClientId;

    @Value("${spring.security.oauth2.client.registration.github.client-secret:}")
    private String githubClientSecret;

    @Bean
    @ConditionalOnProperty(name = "oauth2.enabled", havingValue = "true")
    public ClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration.Builder googleBuilder = null;
        ClientRegistration.Builder githubBuilder = null;

        if (googleClientId != null && !googleClientId.isEmpty()) {
            googleBuilder = ClientRegistration.withRegistrationId("google")
                    .clientId(googleClientId)
                    .clientSecret(googleClientSecret)
                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                    .redirectUri("{baseUrl}/api/auth/oauth2/callback/{registrationId}")
                    .scope("openid", "profile", "email")
                    .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
                    .tokenUri("https://www.googleapis.com/oauth2/v4/token")
                    .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                    .userNameAttributeName("sub")
                    .clientName("Google");
        }

        if (githubClientId != null && !githubClientId.isEmpty()) {
            githubBuilder = ClientRegistration.withRegistrationId("github")
                    .clientId(githubClientId)
                    .clientSecret(githubClientSecret)
                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                    .redirectUri("{baseUrl}/api/auth/oauth2/callback/{registrationId}")
                    .scope("read:user", "user:email")
                    .authorizationUri("https://github.com/login/oauth/authorize")
                    .tokenUri("https://github.com/login/oauth/access_token")
                    .userInfoUri("https://api.github.com/user")
                    .userNameAttributeName("id")
                    .clientName("GitHub");
        }

        if (googleBuilder != null && githubBuilder != null) {
            return new InMemoryClientRegistrationRepository(googleBuilder.build(), githubBuilder.build());
        } else if (googleBuilder != null) {
            return new InMemoryClientRegistrationRepository(googleBuilder.build());
        } else if (githubBuilder != null) {
            return new InMemoryClientRegistrationRepository(githubBuilder.build());
        }

        // Fallback - shouldn't reach here due to conditional property
        return new InMemoryClientRegistrationRepository();
    }
}
