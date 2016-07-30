package nc.gouv.drdnc.ilda.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Java config spring-data-rest
 *
 * @author ILDA
 */
@Component
public class IldaRestConfig extends RepositoryRestConfigurerAdapter {

    private static final String ETAG = "ETag";

    /**
     * Méthode permetant la configuration du RepositoryRestConfiguration
     *
     * @param config RepositoryRestConfiguration
     */
    @Override
    public void configureRepositoryRestConfiguration(final RepositoryRestConfiguration config) {
        config.setBasePath("/api");

        // Retour de l'objet en création, mais pas en modification
        config.setReturnBodyOnCreate(Boolean.TRUE);
        config.setReturnBodyOnUpdate(Boolean.FALSE);
        config.setDefaultPageSize(10);
    }

    /**
     * Gestion cors avec spring data rest
     *
     * @return CorsFilter
     */
    @Bean
    public CorsFilter corsFilter() {

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(Boolean.TRUE);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addExposedHeader(ETAG);
        config.addAllowedMethod(HttpMethod.GET);
        config.addAllowedMethod(HttpMethod.POST);
        config.addAllowedMethod(HttpMethod.PUT);
        config.addAllowedMethod(HttpMethod.DELETE);
        config.addAllowedMethod(HttpMethod.OPTIONS);
        config.addAllowedMethod(HttpMethod.HEAD);
        config.addAllowedMethod(HttpMethod.PATCH);
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
