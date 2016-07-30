package nc.gouv.drdnc.ilda.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Déclaration du Repository JPA pour le projet ILDA Création de la Factory de l'entity manager Instanciation du transaction manager
 * 
 *
 * @author ILDA
 */
@Configuration
@EnableJpaRepositories(basePackages = "nc.gouv.drdnc.ilda", entityManagerFactoryRef = "ildaEntityManagerFactory", transactionManagerRef = "ildaTransactionManager")
public class JpaRepositoryIlda {

    @Autowired
    @Qualifier("ildaDataSource")
    private DataSource ildaDataSource;

    @Autowired
    private JpaVendorAdapter ildaJpaVendorAdapter;

    /**
     * Factory de l'entity manager pour le projet ILDA
     *
     * @return LocalContainerEntityManagerFactoryBean initialisé avec la datasource ILDA
     */
    @Primary
    @Bean(name = "ildaEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean ildaEntityManagerFactory() {

        final LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();

        entityManager.setDataSource(ildaDataSource);
        entityManager.setPersistenceUnitName("ilda");
        entityManager.setJpaVendorAdapter(ildaJpaVendorAdapter);
        return entityManager;
    }

    /**
     * Instancie et fournit le TransactionManager du projet
     *
     * @return le PlatformTransactionManager pour le projet
     */
    @Primary
    @Bean
    public PlatformTransactionManager ildaTransactionManager() {
        final JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(ildaEntityManagerFactory().getObject());
        return txManager;
    }

}
