/**
 *
 */
package nc.gouv.drdnc.ilda.loader.metier.referentiel.unite;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import nc.gouv.drdnc.ilda.metier.referentiel.unite.Unite;
import nc.gouv.drdnc.ilda.metier.referentiel.unite.UniteRepository;

/**
 * Loader de données pour le référentiel Unités
 *
 * @author ILDA
 */
@Component
public class UniteLoader {

    private static final String METRE = "Mètre";

    private static final String M = "M";

    private static final String METRE_CARRE = "Mètre carré";

    private static final String M2 = "M2";

    private static final String METRE_CUBE = "Mètre cube";

    private static final String M3 = "M3";

    private static final String LITRE = "Litre";

    private static final String L = "L";

    private static final String KILOGRAMME = "Kilogramme";

    private static final String KG = "KG";

    private List<Unite> lUnites = new ArrayList<>();

    @Autowired
    UniteRepository uniteRepo;

    /**
     * Création et enregistrement d'une Unité
     *
     * @param code Code
     * @param designation Désignation
     * @param dateEffet Date d'effet
     * @param dateFinEffet Date de fin d'effet
     * @return l'entité créée si elle n'existe pas déjà, sinon celle de la BDD
     */
    public Unite createUnite(final String code, final String designation, final LocalDate dateEffet, final LocalDate dateFinEffet) {

        // Vérifier si l'Unité existe déjà. Si oui, la réuitiliser
        Unite entt = uniteRepo.findByCodeAndDateEffet(code, dateEffet);

        if (entt == null) {
            entt = new Unite();
            entt.setCode(code);
            entt.setDesignation(designation);
            entt.setDateEffet(dateEffet);
            entt.setDateFinEffet(dateFinEffet);

            entt = uniteRepo.save(entt);
        }

        lUnites.add(entt);

        return entt;
    }

    /**
     * Chargement de données
     */
    public void loadData() {
        // Masse
        createUnite(KG, KILOGRAMME, LocalDate.of(2013, 1, 1), LocalDate.of(2013, 12, 31));
        createUnite(KG, KILOGRAMME, LocalDate.of(2014, 1, 1), LocalDate.of(2014, 12, 31));
        createUnite(KG, KILOGRAMME, LocalDate.of(2015, 1, 1), LocalDate.of(2015, 12, 31));
        createUnite(KG, KILOGRAMME, LocalDate.of(2016, 1, 1), LocalDate.of(2016, 12, 31));
        createUnite(KG, KILOGRAMME, LocalDate.of(2035, 1, 1), null);

        // Litres
        createUnite(L, LITRE, LocalDate.of(2013, 1, 1), LocalDate.of(2013, 12, 31));
        createUnite(L, LITRE, LocalDate.of(2014, 1, 1), LocalDate.of(2014, 12, 31));
        createUnite(L, LITRE, LocalDate.of(2015, 1, 1), LocalDate.of(2015, 12, 31));
        createUnite(L, LITRE, LocalDate.of(2016, 1, 1), LocalDate.of(2016, 12, 31));
        createUnite(L, LITRE, LocalDate.of(2035, 1, 1), null);

        // Volume
        createUnite(M, METRE, LocalDate.of(2013, 1, 1), LocalDate.of(2013, 12, 31));
        createUnite(M, METRE, LocalDate.of(2014, 1, 1), LocalDate.of(2014, 12, 31));
        createUnite(M, METRE, LocalDate.of(2015, 1, 1), LocalDate.of(2015, 12, 31));
        createUnite(M, METRE, LocalDate.of(2016, 1, 1), LocalDate.of(2016, 12, 31));
        createUnite(M, METRE, LocalDate.of(2035, 1, 1), null);

        // Volume
        createUnite(M2, METRE_CARRE, LocalDate.of(2013, 1, 1), LocalDate.of(2013, 12, 31));
        createUnite(M2, METRE_CARRE, LocalDate.of(2014, 1, 1), LocalDate.of(2014, 12, 31));
        createUnite(M2, METRE_CARRE, LocalDate.of(2015, 1, 1), LocalDate.of(2015, 12, 31));
        createUnite(M2, METRE_CARRE, LocalDate.of(2016, 1, 1), LocalDate.of(2016, 12, 31));
        createUnite(M2, METRE_CARRE, LocalDate.of(2035, 1, 1), null);

        // Volume
        createUnite(M3, METRE_CUBE, LocalDate.of(2013, 1, 1), LocalDate.of(2013, 12, 31));
        createUnite(M3, METRE_CUBE, LocalDate.of(2014, 1, 1), LocalDate.of(2014, 12, 31));
        createUnite(M3, METRE_CUBE, LocalDate.of(2015, 1, 1), LocalDate.of(2015, 12, 31));
        createUnite(M3, METRE_CUBE, LocalDate.of(2016, 1, 1), LocalDate.of(2016, 12, 31));
        createUnite(M3, METRE_CUBE, LocalDate.of(2035, 1, 1), null);
    }

    /**
     * Suppression des données
     */
    public void removeData() {
        uniteRepo.delete(lUnites);
    }
}
