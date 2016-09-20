package nc.gouv.drdnc.ilda.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 * Configuration de l'implémentation JPA du projet. Hibernate dans le cas présent
 *
 * @author ILDA
 */
@Configuration
public class IldaJpaVendorAdapter {

    @Value("${ilda.show.sql:false}")
    private boolean showSql;

    @Autowired
    private HibernateJpaVendorAdapter hibernateJVA;

    /**
     * Instanciation de l'implémentation JPA du projet (Hibernate)
     *
     * @return Instance de HibernateJpaVendorAdapter
     */
    @Bean
    @Primary
    public JpaVendorAdapter jpaVendorAdapterIlda() {
        // Utilisation d'une BDD Postgresql
        hibernateJVA.setShowSql(showSql);
        hibernateJVA.setDatabase(Database.H2);
        hibernateJVA.setGenerateDdl(true);
        hibernateJVA.setDatabasePlatform("org.hibernate.dialect.H2Dialect");
        return hibernateJVA;
    }

}
