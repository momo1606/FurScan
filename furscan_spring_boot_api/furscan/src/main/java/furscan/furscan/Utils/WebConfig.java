package furscan.furscan.Utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static furscan.furscan.Constants.MAX_AGE;

/**
 * Cors configuration
 */
@Configuration
@EnableWebMvc
public class WebConfig {
     @Bean
     public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setMaxAge(MAX_AGE);
        source.registerCorsConfiguration("/**",config);
        // CorsFilter bean = new CorsFilter(source);
        // bean.setOrder(-102);
        return new CorsFilter(source);
    }
}
