package nc.gouv.drdnc.ilda.metier.tarifs.catalogue.chapitre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import nc.gouv.drdnc.ilda.metier.tarifs.catalogue.libellechapitre.LibelleChapitre;
import nc.gouv.drdnc.ilda.metier.tarifs.catalogue.libellechapitre.LibelleChapitreRepository;

/**
 * Actions à faire lors de l'envoi des données d'un chapitre
 *
 * @author ILDA
 */
@Component
@RepositoryEventHandler(Chapitre.class)
public class ChapitreEventListener {

    @Autowired
    private LibelleChapitreRepository libelleChapitreRepo;

    private static final String MOTIF_DEFAUT = "Libellé initial";

    /**
     * Création du libelle chapitre initial
     *
     * @param chapitre Chapitre créé
     */
    @HandleAfterCreate
    public void handleAfterCreate(final Chapitre chapitre) {
        // Enregistrement des libellés chapitres
        if (chapitre.getDesignationLibelleDefaut() != null) {
            LibelleChapitre defaultLibelleChapitre = new LibelleChapitre();
            defaultLibelleChapitre.setDateEffet(chapitre.getDateEffet());
            defaultLibelleChapitre.setMotifEffet(MOTIF_DEFAUT);
            defaultLibelleChapitre.setDesignation(chapitre.getDesignationLibelleDefaut());
            defaultLibelleChapitre.setNotes(chapitre.getNotesLibelleDefaut());
            defaultLibelleChapitre.setChapitre(chapitre);
            libelleChapitreRepo.save(defaultLibelleChapitre);
        }
    }

}
