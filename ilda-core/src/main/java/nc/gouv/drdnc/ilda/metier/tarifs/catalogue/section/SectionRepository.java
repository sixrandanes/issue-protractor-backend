package nc.gouv.drdnc.ilda.metier.tarifs.catalogue.section;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * Repository des Sections
 *
 * @author ILDA
 */
@RepositoryRestResource(path = "sections", collectionResourceRel = "sections")
public interface SectionRepository extends JpaRepository<Section, UUID> {

    @RestResource(path = "sections_valid")
    @Query(value = "Select s from Section s where"
            + " (CAST(:dateEffet as string) = '') "
            + "   OR (TO_DATE(:dateEffet, 'dd/MM/yyyy') BETWEEN s.dateEffet AND s.dateFinEffet) ")
    List<Section> findSectionValid(@Param("dateEffet") String dateEffet);

}
