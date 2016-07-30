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
	@Profile({ "local", "jenkins" })
	public Flyway flywayTest() {
		// En Local et test, on supprime la BDD pour la recréer entièrement
		flywayBean.setDataSource(ildaDataSource);
		flywayBean.setSchemas("ILDA");
		flywayBean.clean();
		flywayBean.migrate();
		return flywayBean;
	}

	/**
	 * Instanciation et initialisation de FLyWay pour les profils - production -
	 * dev - itg
	 *
	 * @return L'instance de FlyWay initialisé pour le projet
	 */
	@Bean(name = "flyway")
	@Profile({ "production", "develop" })
	public Flyway flyway() {
		// Sur les environnements d'integ, valid, prod, etc., on ne fait que la
		// migration incrémentale
		flywayBean.setDataSource(ildaDataSource);
		flywayBean.setSchemas("ILDA");

		// En cas d'erreur, réparer la BDD
		if (flywayBean.info() != null && flywayBean.info().current() != null
				&& MigrationState.FAILED == flywayBean.info().current().getState()) {

			flywayBean.repair();
		}
		flywayBean.setOutOfOrder(true);
		flywayBean.migrate();
		return flywayBean;
	}
}
