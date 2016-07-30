package nc.gouv.drdnc.ilda.metier.referentiel.tauxchange;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import nc.gouv.drdnc.ilda.metier.referentiel.devise.Devise;

/**
 * Repository des Taux de Change
 *
 * @author ILDA
 */
@RepositoryRestResource(path = "taux_changes", collectionResourceRel = "taux_changes")
public interface TauxChangeRepository extends JpaRepository<TauxChange, UUID> {

    /**
     * Compte le nombre de taux de change avec une date d'effet pour une devise donnée
     * 
     * @param dateEffet date d'effet dont il faut vérifier l'unicité
     * @param devise la devise pour laquelle on compte le nombre de taux de change
     * @return le nombre de taux de change avec une date d'effet pour une devise donnée
     */
    @RestResource(exported = false)
    @Query(value = "Select count(*) from TauxChange txChange where txChange.dateEffet = :dateEffet and txChange.devise = :devise")
    int countUniqueDateEffet(@Param("dateEffet") LocalDate dateEffet, @Param("devise") Devise devise);

    /**
     * Recherche d'un taux de change pour une devise avec une date d'effet
     * 
     * @param devise La devise pour laquelle on recherche le taux de change
     * @param dateEffet La date d'effet pour laquelle on recherche le taux de change
     * @return Le taux de change, si il existe, répondant aux critères passés
     */
    @RestResource(exported = false)
    TauxChange findByDeviseAndDateEffet(Devise devise, LocalDate dateEffet);
}
