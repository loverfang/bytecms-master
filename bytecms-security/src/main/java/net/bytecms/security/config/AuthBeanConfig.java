package net.bytecms.security.config;

import net.bytecms.security.custom.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class AuthBeanConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        CustomJwtAccessTokenConverter jwtAccessTokenConverter = new CustomJwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey("j83jxnjsleubf73fdsEWrtsduids");
        return jwtAccessTokenConverter;
    }

    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
    }

    @Bean
    AccessDecisionManager accessDecisionManager() {
        return new CustomAccessDecisionManager();
    }

    @Bean
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setReuseRefreshToken(true);
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setTokenStore(tokenStore());
        return defaultTokenServices;
    }

    @Bean
    public TokenStore tokenStore() {
        return new CustomJwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public TokenExtractor tokenExtractor() {
        return new BearerTokenExtractor();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenDeniedEntryPoint();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return  new CustomAccessDeniedHandler();
    }

    @Bean
    public CustomSecurityMetadataSource customSecurityMetadataSource() {
        return new CustomSecurityMetadataSource();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new CustomUserAuthenticationProvider();
    }

}
