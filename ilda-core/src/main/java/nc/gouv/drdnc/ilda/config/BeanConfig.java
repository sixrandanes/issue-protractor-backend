package nc.gouv.drdnc.ilda.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 * Classe pour d'initialisation des bean de config
 *
 * @author ILDA
 */
@Configuration
public class BeanConfig {

    /**
     * @return init bean config flyway
     */
    @Bean(name = "flywayBeanConfig")
    public Flyway flywayBean() {
        return new Flyway();
    }

    /**
     * 
     * @return init bean config dataSrouce
     */
    @Bean(name = "dataSourceBean")
    public JndiDataSourceLookup dataSourceBean() {
        return new JndiDataSourceLookup();
    }

    /**
     * 
     * @return init bean config jpaVendor
     */
    @Bean(name = "hibernateJVABean")
    public HibernateJpaVendorAdapter hibernateJVABean() {
        return new HibernateJpaVendorAdapter();
    }

}
