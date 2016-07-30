package nc.gouv.drdnc.ilda.metier.tarifs.catalogue.chapitre;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import nc.gouv.drdnc.ilda.commons.historisable.HistorisableRepository;
import nc.gouv.drdnc.ilda.metier.tarifs.catalogue.section.Section;

/**
 * Repository des chapitres
 *
 * @author ILDA
 */
@RepositoryRestResource(path = "chapitres", collectionResourceRel = "chapitres")
public interface ChapitreRepository extends HistorisableRepository<Chapitre, UUID> {

    /**
     * Recherche d'un chapitre par son numero
     * 
     * @param numeroChapitre : numero du chapitre
     * @return chapitre correspondant aux critères
     */
    @RestResource(exported = false)
    List<Chapitre> findByNumeroChapitre(Integer numeroChapitre);

    /**
     * Recherche d'un chapitre par son numero et sa date d'effet
     * 
     * @param numeroChapitre : numero du chapitre
     * @param section : section liée au chapitre
     * @param dateEffet : date effet
     * @return Chapitre correspondant aux critères
     */
    @RestResource(exported = false)
    Chapitre findByNumeroChapitreAndSectionAndDateEffet(Integer numeroChapitre, Section section, LocalDate dateEffet);

    /**
     * Recherche des Chapitres en cours de validité
     *
     * @return Liste des Chapitres dont la date d'effet est inférieure ou égale à la date du jour et dont la date de fin
     * d'effet est supérieure ou égale à la date du jour, ou nulle.
     */
    @Override
    @RestResource(path = "valid")
    @Query("Select c from Chapitre c where "
            + " c.dateEffet <= current_date "
            + " AND (c.dateFinEffet >= current_date OR c.dateFinEffet is null) ")
    List<Chapitre> findValid();
}
