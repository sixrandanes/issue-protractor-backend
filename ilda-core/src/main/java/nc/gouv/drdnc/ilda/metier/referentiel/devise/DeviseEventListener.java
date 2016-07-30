package nc.gouv.drdnc.ilda.metier.referentiel.devise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import nc.gouv.drdnc.ilda.metier.referentiel.tauxchange.TauxChange;
import nc.gouv.drdnc.ilda.metier.referentiel.tauxchange.TauxChangeRepository;

/**
 * Actions à faire lors de l'envoi des données d'une devise
 *
 * @author ILDA
 */
@Component
@RepositoryEventHandler(Devise.class)
public class DeviseEventListener {

    @Autowired
    private TauxChangeRepository txChangeRepo;

    /**
     * Création du taux de change initial
     *
     * @param dev Devise créée
     */
    @HandleAfterCreate
    public void handleAfterCreate(final Devise dev) {
        // Enregistrement des taux de changes
        if (dev.getTauxInitial() != null) {
            TauxChange defaultTaux = new TauxChange();
            defaultTaux.setDateEffet(dev.getDateEffet());
            defaultTaux.setValeur(dev.getTauxInitial());
            defaultTaux.setDevise(dev);
            txChangeRepo.save(defaultTaux);
        }
    }

}
