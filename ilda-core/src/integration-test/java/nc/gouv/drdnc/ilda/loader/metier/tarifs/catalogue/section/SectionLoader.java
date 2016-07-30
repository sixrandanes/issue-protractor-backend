package nc.gouv.drdnc.ilda.loader.metier.tarifs.catalogue.section;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import nc.gouv.drdnc.ilda.metier.tarifs.catalogue.section.Section;
import nc.gouv.drdnc.ilda.metier.tarifs.catalogue.section.SectionRepository;

@Component
public class SectionLoader {

    @Autowired
    SectionRepository sectionRepo;

    public Section createSection(String numeroSection, LocalDate dateEffet, LocalDate dateFinEffet) {
        Section section = new Section();
        section.setNumeroSection(numeroSection);
        section.setDateEffet(dateEffet);
        section.setDateFinEffet(dateFinEffet);

        sectionRepo.save(section);

        return section;
    }

    /**
     * Suppression de toutes les lignes de section
     */
    public void removeAllData() {
        sectionRepo.deleteAll();
    }
}
