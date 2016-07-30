package nc.gouv.drdnc.ilda.config.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import nc.gouv.drdnc.ilda.config.security.SecureUrls;

/**
 * Configuration de la securite du projet
 *
 * @author ILDA
 */
@Configuration
@Order(0)
public class IldaWebSecurityConfig extends WebSecurityConfigurerAdapter {


    /**
     * {@inheritDoc}
     */
    @Override
    public void configure(final HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/")
                .permitAll()
                // HTTP OPTIONS AUTORISEES
                .antMatchers(HttpMethod.OPTIONS)
                .permitAll()
                // HAL BROWSER
                .antMatchers(HttpMethod.GET, "/api/browser/**")
                .permitAll()
                // REFERENTIEL
                .regexMatchers(HttpMethod.GET, SecureUrls.URL_REFERENTIEL).permitAll()
                .and()
                // La sécurité se base déjà sur un token généré à chaque requête.
                // du coup on limite vachement les possibilités de CSRF.
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
