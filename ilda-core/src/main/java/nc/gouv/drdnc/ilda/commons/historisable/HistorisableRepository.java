/**
 *
 */
package nc.gouv.drdnc.ilda.commons.historisable;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * Repository des Historisables
 *
 * @author ILDA
 * @param <T> Classe implémentant l'interface IHistorisable
 * @param <ID> ID unique
 */
@NoRepositoryBean
public interface HistorisableRepository<T extends IHistorisable, ID> extends JpaRepository<T, UUID> {

    /**
     * Recherche des Entités en cours de validité
     *
     * @return Liste des Entités dont la date d'effet est inférieure ou égale à la date du jour et dont la date de fin
     * d'effet est supérieure ou égale à la date du jour, ou nulle.
     */
    @RestResource(path = "valid")
    // La Query doit être adaptée par chaque repository
    @Query("A redéfinir")
    List<T> findValid();
}
