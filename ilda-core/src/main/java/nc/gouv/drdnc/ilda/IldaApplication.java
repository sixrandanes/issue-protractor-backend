package nc.gouv.drdnc.ilda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.hateoas.config.EnableEntityLinks;

/**
 * Classe principale de l'application ILDA
 *
 * @author ILDA
 */
@SpringBootApplication
@EntityScan(basePackageClasses = { IldaApplication.class, Jsr310JpaConverters.class })
@EnableAspectJAutoProxy
@EnableEntityLinks
public class IldaApplication {

    /**
     * Méthode principale de lancement de l'application
     *
     * @param args arguments
     */
    public static void main(final String... args) {
        SpringApplication.run(IldaApplication.class, args);
    }
}
