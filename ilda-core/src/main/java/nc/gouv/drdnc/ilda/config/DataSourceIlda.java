package nc.gouv.drdnc.ilda.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

/**
 * DataSource du projet ILDA
 *
 * @author ILDA
 */
@Configuration
public class DataSourceIlda {



	/**
	 * Initialise la DataSource du projet sur les environnements suivants: - dev
	 * - local - test - itg
	 *
	 * @return la DataSource du projet
	 */
	@Primary



	@Bean(name = "ildaDataSource")
	@ConfigurationProperties(prefix = "datasource.ilda")
	public DataSource ildaDataSourceLocal() {
		return DataSourceBuilder.create().build();
	}
}
