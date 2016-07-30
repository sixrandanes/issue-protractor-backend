package nc.gouv.drdnc.ilda.loader.metier.tarifs.catalogue.libellechapitre;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import nc.gouv.drdnc.ilda.metier.tarifs.catalogue.chapitre.Chapitre;
import nc.gouv.drdnc.ilda.metier.tarifs.catalogue.libellechapitre.LibelleChapitre;
import nc.gouv.drdnc.ilda.metier.tarifs.catalogue.libellechapitre.LibelleChapitreRepository;

@Component
public class LibelleChapitreLoader {

    @Autowired
    LibelleChapitreRepository libelleChapitreRepo;

    public LibelleChapitre createLibelleChapitre(String designation, String notes, LocalDate dateEffet, String motifEffet, Chapitre chapitre) {
        List<LibelleChapitre> libellesChapitres = libelleChapitreRepo.findByDesignation(designation);

        LibelleChapitre libelleChapitre = libellesChapitres.isEmpty() ? null : libellesChapitres.get(0);

        if (libelleChapitre == null) {
            libelleChapitre = new LibelleChapitre();

            libelleChapitre.setChapitre(chapitre);
            libelleChapitre.setDesignation(designation);
            libelleChapitre.setNotes(notes);
            libelleChapitre.setDateEffet(dateEffet);
            libelleChapitre.setMotifEffet(motifEffet);

            libelleChapitreRepo.save(libelleChapitre);

        }
        return libelleChapitre;

    }

    /**
     * Suppression de toutes les lignes de chapitre
     */
    public void removeAllData() {
        libelleChapitreRepo.deleteAll();
    }

}
