/**
 *
 */
package nc.gouv.drdnc.ilda.metier.referentiel.incoterm;

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
 * Repository des incoterms
 *
 * @author ILDA
 */
@RepositoryRestResource(path = "incoterms", collectionResourceRel = "incoterms")
public interface IncotermRepository extends HistorisableRepository<Incoterm, UUID> {

    /**
     * Recherche des Incoterms en cours de validité
     *
     * @return Liste des Incoterms dont la date d'effet est inférieure ou égale à la date du jour et dont la date de fin d'effet est supérieure ou égale à la
     *         date du jour, ou nulle.
     */
    @Override
    @RestResource(path = "valid")
    @Query("Select i from Incoterm i where " + " i.dateEffet <= current_date "
            + " AND (i.dateFinEffet >= current_date OR i.dateFinEffet is null) ")
    List<Incoterm> findValid();

    /**
     * Recherche des Incoterms avec paramètres
     *
     * @param code Critère de recherche : champ Code (String)
     * @param designation Critère de recherche : champ Désignation (String)
     * @param date Critère de recherche : validité à une date donnée (Date)
     * @param pageRequest Informations de pagination
     * @return Liste des Incoterms répondant aux critères de recherche
     */
    @RestResource(path = "with_params")
    @Query("Select i from Incoterm i where "
            + " i.code like :code || '%' "
            + " AND lower(i.designation) like '%' || lower(:designation) || '%' "
            + " AND ( (CAST(:date as string) = '') "
            + "   OR (TO_DATE(:date, 'dd/MM/yyyy') BETWEEN i.dateEffet AND i.dateFinEffet) "
            + "   OR (TO_DATE(:date, 'dd/MM/yyyy') >= i.dateEffet AND i.dateFinEffet is null) ) ")
    Page<Incoterm> searchWithParams(@Param("code") String code, @Param("designation") String designation, @Param("date") String date, Pageable pageRequest);

    /**
     * Liste des Incoterms ayant un certain code ou une certain désignation
     *
     * @param code Code de l'entité
     * @param designation Désignation de l'entité
     * @return Liste des Incoterms ayant un certain code ou une certain désignation
     */
    @RestResource(exported = false)
    @Query(value = "Select entt from Incoterm entt where (entt.code = :code OR entt.designation = :designation) ")
    List<Incoterm> findByCodeOrDesignation(@Param("code") String code, @Param("designation") String designation);

    /**
     * Recherche d'un incoterm par son code et sa date d'effet
     *
     * @param code Critère de recherche : code de l'incoterm
     * @param dateEffet Critère de recherche : date d'effet de l'incoterm
     * @return L'incoterm, si il existe, répondant aux critères passés
     */
    @RestResource(exported = false)
    Incoterm findByCodeAndDateEffet(String code, LocalDate dateEffet);
}
