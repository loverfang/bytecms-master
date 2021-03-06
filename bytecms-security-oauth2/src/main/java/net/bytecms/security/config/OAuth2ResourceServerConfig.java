package net.bytecms.security.config;

import java.util.List;

import net.bytecms.security.custom.CustomJwtAuthenticationFilter;
import net.bytecms.security.custom.CustomJwtHandler;
import net.bytecms.security.custom.CustomSecurityMetadataSource;
import net.bytecms.security.properties.PermitAllUrlProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.stereotype.Component;


@Component
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private DefaultTokenServices tokenServices;

    @Autowired
    TokenExtractor tokenExtractor;

    @Autowired
    private JwtTokenStore tokenStore;

    @Autowired
    AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    AccessDeniedHandler accessDeniedHandler;

    @Autowired
    AccessDecisionManager accessDecisionManager;

    @Autowired
    CustomSecurityMetadataSource customSecurityMetadataSource;

    @Autowired
    CustomJwtHandler customJwtHandler;

    @Autowired
    PermitAllUrlProperties permitAllUrlProperties;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources
            .tokenServices(tokenServices)
            .tokenExtractor(tokenExtractor)
            .authenticationEntryPoint(authenticationEntryPoint)
            .accessDeniedHandler(accessDeniedHandler);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        List<String> permitAllEndpointList = permitAllUrlProperties.ignores();
        CustomJwtAuthenticationFilter filter = new CustomJwtAuthenticationFilter(null, tokenStore, customJwtHandler);
        http
            .authorizeRequests()
            .antMatchers( permitAllEndpointList.<String>toArray(new String[permitAllEndpointList.size()]) ).permitAll()
            .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                @Override
                public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                    object.setAccessDecisionManager(accessDecisionManager);
                    object.setSecurityMetadataSource(customSecurityMetadataSource);
                    return object;
                }
            }).and()

            .addFilterBefore(filter, FilterSecurityInterceptor.class);
    }

}
