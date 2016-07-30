/**
 *
 */
package nc.gouv.drdnc.ilda.metier.referentiel.unite;

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
 * Repository des Unités
 *
 * @author ILDA
 */
@RepositoryRestResource(path = "unites", collectionResourceRel = "unites")
public interface UniteRepository extends HistorisableRepository<Unite, UUID> {

    /**
     * Recherche des Unites en cours de validité
     *
     * @return Liste des Unites dont la date d'effet est inférieure ou égale à la date du jour et dont la date de fin
     * d'effet est supérieure ou égale à la date du jour, ou nulle.
     */
    @Override
    @RestResource(path = "valid")
    @Query("Select u from Unite u where "
            + " u.dateEffet <= current_date "
            + " AND (u.dateFinEffet >= current_date OR u.dateFinEffet is null) ")
    List<Unite> findValid();

    /**
     * Recherche des Unites avec paramètres
     *
     * @param code Critère de recherche : champ Code (String)
     * @param designation Critère de recherche : champ Désignation (String)
     * @param date Critère de recherche : validité à une date donnée (Date)
     * @param pageRequest
     *
     * @return Liste des Unites répondant aux critères de recherche
     */
    @RestResource(path = "with_params")
    @Query("Select u from Unite u where "
            + " u.code like :code || '%' "
            + " AND lower(u.designation) like '%' || lower(:designation) || '%' "
            + " AND ( (CAST(:date as string) = '') "
            + "   OR (TO_DATE(:date, 'dd/MM/yyyy') BETWEEN u.dateEffet AND u.dateFinEffet) "
            + "   OR (TO_DATE(:date, 'dd/MM/yyyy') >= u.dateEffet AND u.dateFinEffet is null) ) ")
    Page<Unite> searchWithParams(@Param("code") String code, @Param("designation") String designation, @Param("date") String date, Pageable pageRequest);

    /**
     * Liste des l'Unités ayant un certain code ou une certain désignation
     *
     * @param code Code de l'entité
     * @param designation Désignation de l'entité
     * @return Liste des l'Unités ayant un certain code ou une certain désignation
     */
    @RestResource(exported = false)
    @Query(value = "Select entt from Unite entt where (entt.code = :code OR entt.designation = :designation) ")
    List<Unite> findByCodeOrDesignation(@Param("code") String code, @Param("designation") String designation);

    /**
     * Recherche d'une Unité par son code et sa date d'effet
     *
     * @param code Critère de recherche : code de l'Unité
     * @param dateEffet Critère de recherche : date d'effet de l'Unité
     * @return L'unité, si elle existe, répondant aux critères passés
     */
    @RestResource(exported = false)
    Unite findByCodeAndDateEffet(String code, LocalDate dateEffet);
}
