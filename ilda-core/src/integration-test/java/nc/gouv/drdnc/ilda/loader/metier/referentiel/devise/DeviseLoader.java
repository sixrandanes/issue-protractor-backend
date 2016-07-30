package nc.gouv.drdnc.ilda.loader.metier.referentiel.devise;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import nc.gouv.drdnc.ilda.metier.referentiel.devise.Devise;
import nc.gouv.drdnc.ilda.metier.referentiel.devise.DeviseRepository;

/**
 * Loader de données pour le référentiel Devises
 *
 * @author ILDA
 */
@Component
public class DeviseLoader {

    @Autowired
    DeviseRepository deviseRepo;

    private List<Devise> listeDevises = new ArrayList<>();

    public Devise createDevise(final String code, final String designation, final LocalDate dateEffet, final LocalDate dateFinEffet) {
        // Vérifier si l'Unité existe déjà. Si oui, la réuitiliser
        Devise entt = deviseRepo.findByCodeAndDateEffet(code, dateEffet);

        if (entt == null) {
            entt = new Devise();
            entt.setCode(code);
            entt.setDesignation(designation);
            entt.setDateEffet(dateEffet);
            entt.setDateFinEffet(dateFinEffet);

            entt = deviseRepo.save(entt);
        }

        listeDevises.add(entt);

        return entt;
    }

    /**
     * Suppression des données ajoutées
     */
    public void removeData() {
        deviseRepo.delete(listeDevises);
    }

    /**
     * Suppression des données
     */
    public void removeAllData() {
        deviseRepo.deleteAll();
    }

    /**
     * Chargement des données
     */
    public void loadData() {
        // XPF
        this.createDevise("XPF", "Franc Pacifique", LocalDate.now().minusYears(5), LocalDate.now().minusYears(4));
        this.createDevise("XPF", "Franc Pacifique", LocalDate.now().minusYears(3), LocalDate.now().minusYears(2));
        this.createDevise("XPF", "Franc Pacifique", LocalDate.now().minusYears(2), LocalDate.now().minusYears(1));
        this.createDevise("XPF", "Franc Pacifique", LocalDate.now().plusYears(1), LocalDate.now().plusYears(2));

        // ANCIEN FRANC
        this.createDevise("AFR", "Ancien Franc", LocalDate.now().minusYears(5), LocalDate.now().minusYears(4));
        this.createDevise("AFR", "Ancien Franc", LocalDate.now().minusYears(3), LocalDate.now().minusYears(2));
        this.createDevise("AFR", "Ancien Franc", LocalDate.now().minusYears(2), LocalDate.now().minusYears(1));
        this.createDevise("AFR", "Ancien Franc", LocalDate.now().plusYears(1), LocalDate.now().plusYears(2));

        // FRANCS
        this.createDevise("FRC", "Nouveau Franc", LocalDate.now().minusYears(5), LocalDate.now().minusYears(4));
        this.createDevise("FRC", "Nouveau Franc", LocalDate.now().minusYears(3), LocalDate.now().minusYears(2));
        this.createDevise("FRC", "Nouveau Franc", LocalDate.now().minusYears(2), LocalDate.now().minusYears(1));
        this.createDevise("FRC", "Nouveau Franc", LocalDate.now().plusYears(1), LocalDate.now().plusYears(2));

        // FRANCS
        this.createDevise("EUR", "Euros", LocalDate.now().minusYears(5), LocalDate.now().minusYears(4));
        this.createDevise("EUR", "Euros", LocalDate.now().minusYears(3), LocalDate.now().minusYears(2));
        this.createDevise("EUR", "Euros", LocalDate.now().minusYears(2), LocalDate.now().minusYears(1));
        this.createDevise("EUR", "Euros", LocalDate.now().plusYears(1), LocalDate.now().plusYears(2));

        // US DOLLAR
        this.createDevise("USD", "Dollar Américain", LocalDate.now().minusYears(5), LocalDate.now().minusYears(4));
        this.createDevise("USD", "Dollar Américain", LocalDate.now().minusYears(3), LocalDate.now().minusYears(2));
        this.createDevise("USD", "Dollar Américain", LocalDate.now().minusYears(2), LocalDate.now().minusYears(1));
        this.createDevise("USD", "Dollar Américain", LocalDate.now().plusYears(1), LocalDate.now().plusYears(2));
    }

}
