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
import nc.gouv.drdnc.ilda.metier.referentiel.unite.Unite;

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
     * @param code2 Critère de recherche : champ Code2 (String)
     * @param code3 Critère de recherche : champ Code3 (String)
     * @param designation Critère de recherche : champ Désignation (String)
     * @param date Critère de recherche : validité à une date donnée (Date)
     *
     * @return Liste des Pays répondant aux critères de recherche
     */
    @RestResource(path = "with_params")
    @Query("Select p from Pays p where " + " p.code2 like :code2 || '%' " + " AND p.code3 like :code3 || '%' "
            + " AND lower(p.designation) like '%' || lower(:designation) || '%' "
            + " AND ( (CAST(:date as string) = '') "
            + "   OR (TO_DATE(:date, 'dd/MM/yyyy') BETWEEN p.dateEffet AND p.dateFinEffet) "
            + "   OR (TO_DATE(:date, 'dd/MM/yyyy') >= p.dateEffet AND p.dateFinEffet is null) ) ")
    Page<Pays> searchWithParams(@Param("code2") String code2, @Param("code3") String code3,
            @Param("designation") String designation, @Param("date") String date, Pageable pageRequest);

    /**
     * Liste des Pays ayant un certain code2 ou un certain code3
     *
     * @param code2 Code2 du Pays
     * @param code3 Code3 du Pays
     * @return Liste des l'Unités ayant un certain code ou une certain désignation
     */
    @RestResource(exported = false)
    @Query(value = "Select entt from Pays entt where (entt.code2 = :code2 OR entt.code3 = :code3) ")
    List<Unite> findByCode2Or3(@Param("code2") String code2, @Param("code3") String code3);

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
