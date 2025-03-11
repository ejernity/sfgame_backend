package gr.nlamp.sfgame_backend.cors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@PropertySource("classpath:cors.properties")
public class CorsConfig {
    @Value("#{'${cors.allowedHeadersList}'.split(';')}")
    private List<String> allowedHeadersList;
    
    @Value("#{'${cors.exposedHeadersList}'.split(';')}")
    private List<String> exposedHeadersList;

    @Value("#{'${cors.allowedMethodsList}'.split(';')}")
    private List<String> allowedMethodsList;
    @Value("#{'${cors.crossOriginUrlList}'.split(';')}")
    private List<String> crossOriginUrlList;

    @Value("${cors.allowCredentials}")
    private Boolean allowCredentials;

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(allowCredentials);
        corsConfiguration.setAllowedOrigins(crossOriginUrlList);
        corsConfiguration.setAllowedHeaders(allowedHeadersList);
        corsConfiguration.setExposedHeaders(exposedHeadersList);
        corsConfiguration.setAllowedMethods(allowedMethodsList);
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }
}
