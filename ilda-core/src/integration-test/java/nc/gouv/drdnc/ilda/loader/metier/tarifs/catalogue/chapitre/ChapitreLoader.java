package nc.gouv.drdnc.ilda.loader.metier.tarifs.catalogue.chapitre;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import nc.gouv.drdnc.ilda.loader.metier.tarifs.catalogue.section.SectionLoader;
import nc.gouv.drdnc.ilda.metier.tarifs.catalogue.chapitre.Chapitre;
import nc.gouv.drdnc.ilda.metier.tarifs.catalogue.chapitre.ChapitreRepository;
import nc.gouv.drdnc.ilda.metier.tarifs.catalogue.libellechapitre.LibelleChapitre;
import nc.gouv.drdnc.ilda.metier.tarifs.catalogue.libellechapitre.LibelleChapitreRepository;
import nc.gouv.drdnc.ilda.metier.tarifs.catalogue.section.Section;
import nc.gouv.drdnc.ilda.metier.tarifs.catalogue.section.SectionRepository;

/**
 * Loader de données pour le référentiel Chapitres
 *
 * @author ILDA
 */
@Component
public class ChapitreLoader {

    @Autowired
    ChapitreRepository chapitreRepo;

    @Autowired
    SectionRepository sectionRepo;

    @Autowired
    LibelleChapitreRepository libelleChapitreRepo;

    @Autowired
    SectionLoader sectionLoader;

    public Chapitre createChapitre(Integer numeroChapitre, Section section, LocalDate dateEffet, LocalDate dateFinEffet, String motifEffet,
            String motifFinEffet, String designationLibelleChapitre) {
        Chapitre chap = chapitreRepo.findByNumeroChapitreAndSectionAndDateEffet(numeroChapitre, section, dateEffet);

        if (chap == null) {
            chap = new Chapitre();
            chap.setNumeroChapitre(numeroChapitre);
            chap.setSection(section);
            chap.setDateEffet(dateEffet);
            chap.setMotifEffet(motifEffet);
            chap.setDateFinEffet(dateFinEffet);
            chap.setMotifFinEffet(motifFinEffet);

            Chapitre chapCreated = chapitreRepo.save(chap);

            LibelleChapitre libelleChap = new LibelleChapitre();
            libelleChap.setChapitre(chapCreated);
            libelleChap.setDesignation(designationLibelleChapitre);
            libelleChap.setDateEffet(chap.getDateEffet());
        }
        return chap;

    }

    /**
     * Suppression de toutes les lignes de chapitre
     */
    public void removeAllData() {
        chapitreRepo.deleteAll();
    }

    public void loadData() {
        Section section1 = sectionLoader.createSection("I", LocalDate.now().plusDays(1), LocalDate.now().plusMonths(5));
        this.createChapitre(new Integer(15), section1, LocalDate.now().plusDays(1), LocalDate.now().plusDays(4), "motif 1", "motif fin 1", "poisson");

        Section section2 = sectionLoader.createSection("IV", LocalDate.now().plusDays(15), LocalDate.now().plusMonths(30));
        this.createChapitre(new Integer(45), section2, LocalDate.now().plusDays(15), LocalDate.now().plusDays(28), "motif 2", "motif fin 2 ", "plantes");

        Section section3 = sectionLoader.createSection("VII", LocalDate.now().plusDays(35), LocalDate.now().plusMonths(40));
        this.createChapitre(new Integer(80), section3, LocalDate.now().plusDays(36), LocalDate.now().plusDays(38), "motif 3", "motif fin 3", "singes");

    }

}
