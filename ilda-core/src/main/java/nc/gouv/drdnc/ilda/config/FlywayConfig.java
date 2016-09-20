package nc.gouv.drdnc.ilda.config;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Configuration de FlyWay
 *
 * @author ILDA
 */
@Configuration
public class FlywayConfig {

	@Autowired
	@Qualifier("ildaDataSource")
	private DataSource ildaDataSource;

	@Autowired
	@Qualifier("flywayBeanConfig")
	private Flyway flywayBean;

	/**
	 * Instanciation et initialisation de FLyWay pour les profils - Local - Test
	 *
	 * @return L'instance de FlyWay initialisée pour le projet
	 */
	@Bean(name = "flyway")
	public Flyway flywayTest() {
		// En Local et test, on supprime la BDD pour la recréer entièrement
		flywayBean.setDataSource(ildaDataSource);
		flywayBean.setSchemas("ILDA");
		return flywayBean;
	}

}
