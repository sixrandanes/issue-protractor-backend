package nc.gouv.drdnc.ilda.loader.metier.referentiel.tauxChange;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import nc.gouv.drdnc.ilda.loader.metier.referentiel.devise.DeviseLoader;
import nc.gouv.drdnc.ilda.metier.referentiel.devise.Devise;
import nc.gouv.drdnc.ilda.metier.referentiel.tauxchange.TauxChange;
import nc.gouv.drdnc.ilda.metier.referentiel.tauxchange.TauxChangeRepository;

/**
 * Loader de données pour le référentiel taux de change
 *
 * @author ILDA
 */
@Component
public class TauxChangeLoader {

    @Autowired
    TauxChangeRepository txChangeRepo;

    @Autowired
    DeviseLoader deviseLoader;

    /**
     * Méthode de création d'un taux de change
     * 
     * @param devise La devise lié au taux de change
     * @param valeur la valeur du taux de change
     * @param dateEffet la date d'effet du taux de change
     * @param motifEffet le motif d'effet du taux de change
     * @return l'entité taux de change créée ou trouvée dans la base
     */
    public TauxChange createTauxChange(final Devise devise, final BigDecimal valeur, final LocalDate dateEffet, final String motifEffet) {
        // Vérifier si l'Unité existe déjà. Si oui, la réuitiliser
        TauxChange entt = txChangeRepo.findByDeviseAndDateEffet(devise, dateEffet);
        if (entt == null) {
            entt = new TauxChange();
            entt.setValeur(valeur);
            entt.setDateEffet(dateEffet);
            entt.setMotifEffet(motifEffet);
            entt.setDevise(devise);

            entt = txChangeRepo.save(entt);

        }
        return entt;
    }

    /**
     * Suppression de toutes les lignes de taux de change
     */
    public void removeAllData() {
        txChangeRepo.deleteAll();
    }

    public void loadData() {
        Devise devise = deviseLoader.createDevise("TXC", "Devise loader taux de change", LocalDate.now().plusDays(2), LocalDate.now().plusYears(10));
        this.createTauxChange(devise, new BigDecimal("1234567890.99999"), LocalDate.now().plusDays(10), "Motif 1");
        this.createTauxChange(devise, new BigDecimal("0.99999"), LocalDate.now().plusDays(10), "Motif 2");
        this.createTauxChange(devise, new BigDecimal("01.99999"), LocalDate.now().plusDays(12), "Motif 3");
        this.createTauxChange(devise, new BigDecimal("01.90"), LocalDate.now().plusDays(14), "Motif 4");
        this.createTauxChange(devise, new BigDecimal("01.00009"), LocalDate.now().plusDays(15), "Motif 5");
        this.createTauxChange(devise, new BigDecimal("1.00009"), LocalDate.now().plusDays(16), "Motif 6");
    }
}
