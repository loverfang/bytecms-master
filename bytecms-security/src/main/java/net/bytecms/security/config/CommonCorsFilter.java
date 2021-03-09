package net.bytecms.security.config;


import com.google.common.collect.ImmutableList;
import java.util.List;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Component
@Order(-2147483648)
public class CommonCorsFilter extends CorsFilter {
    public CommonCorsFilter() {
        super((CorsConfigurationSource)configurationSource());
    }

    private static UrlBasedCorsConfigurationSource configurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(Boolean.valueOf(true));
        config.setAllowedOrigins((List)ImmutableList.of("*"));
        config.setAllowedHeaders((List)ImmutableList.of("*"));
        config.setAllowedMethods((List)ImmutableList.of("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
