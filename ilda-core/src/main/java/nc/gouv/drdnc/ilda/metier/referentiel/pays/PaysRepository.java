/**
 *
 */
package nc.gouv.drdnc.ilda.metier.referentiel.pays;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import nc.gouv.drdnc.ilda.commons.historisable.HistorisableRepository;

/**
 * Repository des Pays
 *
 * @author ILDA
 */
@RepositoryRestResource(path = "pays", collectionResourceRel = "pays")
public interface PaysRepository extends HistorisableRepository<Pays, UUID> {

    /**
     * Recherche des pays en cours de validité
     *
     * @return Liste des pays dont la date d'effet est inférieure ou égale à la date du jour et dont la date de fin
     * d'effet est supérieure ou égale à la date du jour, ou nulle.
     */
    @Override
    @RestResource(path = "valid")
    @Query("Select p from Pays p where " + " p.dateEffet <= current_date "
            + " AND (p.dateFinEffet >= current_date OR p.dateFinEffet is null) ")
    List<Pays> findValid();

    /**
     * Recherche des Pays avec paramètres
     *
     * @return Liste des Pays répondant aux critères de recherche
     */
    @RestResource(path = "with_params")
    @Query("Select p from Pays p")
    Page<Pays> searchWithParams(Pageable pageable);

    /**
     * Liste des Pays ayant un certain code2 ou un certain code3
     *
     * @param code2 Code2 du Pays
     * @param code3 Code3 du Pays
     * @return Liste des l'Unités ayant un certain code ou une certain désignation
     */
    @RestResource(exported = false)
    @Query(value = "Select entt from Pays entt where (entt.code2 = :code2 OR entt.code3 = :code3) ")
    List<Pays> findByCode2Or3(@Param("code2") String code2, @Param("code3") String code3);

    /**
     * Recherche d'un Pays par son code2 et sa date d'effet
     *
     * @param code2 Critère de recherche : code de l'Unité
     * @param dateEffet Critère de recherche : date d'effet de l'Unité
     * @return L'unité, si elle existe, répondant aux critères passés
     */
    @RestResource(exported = false)
    Pays findByCode2AndDateEffet(String code2, LocalDate dateEffet);

    /**
     * Recherche d'un Pays par son code3 et sa date d'effet
     *
     * @param code3 Critère de recherche : code de l'Unité
     * @param dateEffet Critère de recherche : date d'effet de l'Unité
     * @return L'unité, si elle existe, répondant aux critères passés
     */
    @RestResource(exported = false)
    Pays findByCode3AndDateEffet(String code3, LocalDate dateEffet);
}
