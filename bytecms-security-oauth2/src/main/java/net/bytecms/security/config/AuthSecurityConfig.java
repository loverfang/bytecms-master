package net.bytecms.security.config;

import net.bytecms.core.utils.Checker;
import java.util.List;

import net.bytecms.security.custom.CustomUserAuthenticationProvider;
import net.bytecms.security.properties.PermitAllUrlProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class AuthSecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    CustomUserAuthenticationProvider customUserAuthenticationProvider;

    @Autowired
    PermitAllUrlProperties permitAllUrlProperties;

    @Override
    public void configure(WebSecurity web) throws Exception {
        List<String> ignores = permitAllUrlProperties.ignores();
        if (Checker.BeNotEmpty(ignores).booleanValue()) {
            web.ignoring().antMatchers(ignores.<String>toArray(new String[ignores.size()]));
        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().
            requestMatchers().anyRequest()
            .and()
            .authorizeRequests()
            .antMatchers(new String[] { "/oauth/*", "/menu/**" }).permitAll();
    }

    /**
     * 指定真正登录处理类
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customUserAuthenticationProvider);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}

