package nc.gouv.drdnc.ilda.metier.referentiel.devise;

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
 * Repository des Devises
 *
 * @author ILDA
 */
@RepositoryRestResource(path = "devises", collectionResourceRel = "devises")
public interface DeviseRepository extends HistorisableRepository<Devise, UUID>{

    /**
     * Compte le nombre de devise avec le même code
     *
     * @param code code de la devise
     * @param dateEffet date d'effet de la devise
     * @param dateFin date de fin d'effet de la devise
     * @param uuid ID de l'entité (null dans le cas d'une création)
     * @return le nombre de devises ayant le même code sur une période la période passée en paramètre
     */
    @RestResource(exported = false)
    @Query(value = "Select count(*) from Devise d where d.code = :code "
            + " AND (d.id != :ID OR cast(:ID as string) is null) "
            + " AND ( (:dateEffet BETWEEN d.dateEffet and d.dateFinEffet) "
            + " OR (:dateFin BETWEEN d.dateEffet and d.dateFinEffet) "
            + " OR (:dateEffet >= d.dateEffet and d.dateFinEffet is null) "
            + " OR (:dateFin >= d.dateEffet and d.dateFinEffet is null) "
            + " OR (d.dateFinEffet is null and CAST(:dateFin as date) is null)"
            + "OR (CAST(:dateFin as date) is null and d.dateEffet >= :dateEffet) )")
    int countUniqueCodeOnPeriod(@Param("code") String code, @Param("dateEffet") LocalDate dateEffet, @Param("dateFin") LocalDate dateFin,
            @Param("ID") UUID uuid);

    /**
     * Recherche des devises avec paramètres
     *
     * @param code Critère de recherche : champ Code (String)
     * @param designation Critère de recherche : champ Désignation (String)
     * @param date Critère de recherche : validité à une date donnée (Date)
     * @param pageRequest
     *
     * @return Liste des devises répondant aux critères de recherche
     */
    @RestResource(path = "with_params")
    @Query("Select d from Devise d where "
            + " d.code like :code || '%' "
            + " AND lower(d.designation) like '%' || lower(:designation) || '%' "
            + " AND ( (CAST(:date as string) = '') "
            + "   OR (TO_DATE(:date, 'dd/MM/yyyy') BETWEEN d.dateEffet AND d.dateFinEffet) "
            + "   OR (TO_DATE(:date, 'dd/MM/yyyy') >= d.dateEffet AND d.dateFinEffet is null) ) ")
    Page<Devise> searchWithParams(@Param("code") String code, @Param("designation") String designation, @Param("date") String date, Pageable pageRequest);

    /**
     * Recherche des Devises en cours de validité
     *
     * @return Liste des Devises dont la date d'effet est inférieure ou égale à la date du jour et dont la date de fin d'effet est supérieure ou égale à la date
     *         du jour, ou nulle.
     */
    @Override
    @RestResource(path = "valid")
    @Query("Select d from Devise d where "
            + " d.dateEffet <= current_date "
            + " AND (d.dateFinEffet >= current_date OR d.dateFinEffet is null) ")
    List<Devise> findValid();

    /**
     * Recherche d'une devise par son code et sa date d'effet
     *
     * @param code Critère de recherche : code de la Devise
     * @param dateEffet Critère de recherche : date d'effet de la Devise
     * @return La Devise, si elle existe, répondant aux critères passés
     */
    @RestResource(exported = false)
    Devise findByCodeAndDateEffet(String code, LocalDate dateEffet);

    /**
     * Liste des Devises ayant un certain code ou une certain désignation
     *
     * @param code Code de l'entité
     * @param designation Désignation de l'entité
     * @return Liste des Devises ayant un certain code ou une certain désignation
     */
    @RestResource(exported = false)
    @Query(value = "Select entt from Devise entt where (entt.code = :code OR entt.designation = :designation) ")
    List<Devise> findByCodeOrDesignation(@Param("code") String code, @Param("designation") String designation);
}
