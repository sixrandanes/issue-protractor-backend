package nc.gouv.drdnc.ilda.metier.tarifs.catalogue.libellechapitre;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import nc.gouv.drdnc.ilda.metier.tarifs.catalogue.chapitre.Chapitre;

/**
 * Repository des Libellés chapitre
 *
 * @author ILDA
 */
@RepositoryRestResource(path = "libelles_chapitres", collectionResourceRel = "libelles_chapitres")
public interface LibelleChapitreRepository extends JpaRepository<LibelleChapitre, UUID> {

    /**
     * recherche si il existe un libellé chapitre avec la même désignation
     * 
     * @param designation la désignation à rechercher
     * @return la liste des libellés avec cette désignation
     */
    @RestResource(exported = false)
    @Query(value = "Select count(*) from LibelleChapitre libelleChapitre where libelleChapitre.designation = :designation )")
    int countLibelleChapitreByDesignation(@Param("designation") String designation);

    /**
     * recherche des libellés pour un chapitre
     * 
     * @param chapitre le chapitre à rechercher
     * @return la liste des libellés avec pour un chapitre
     */
    List<LibelleChapitre> findByChapitre(Chapitre chapitre);

    /**
     * recherche si il exite un libellé chapitre avec la même désignation
     * 
     * @param designation la désignation à rechercher
     * @return la liste des libellés avec cette désignation
     */
    List<LibelleChapitre> findByDesignation(String designation);

}
