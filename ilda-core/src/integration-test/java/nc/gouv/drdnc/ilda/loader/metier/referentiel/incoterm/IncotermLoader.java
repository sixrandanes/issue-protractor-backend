/**
 *
 */
package nc.gouv.drdnc.ilda.loader.metier.referentiel.incoterm;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import nc.gouv.drdnc.ilda.metier.referentiel.incoterm.Incoterm;
import nc.gouv.drdnc.ilda.metier.referentiel.incoterm.IncotermRepository;

/**
 * Loader de données pour le référentiel incoterm
 *
 * @author ILDA
 */
@Component
public class IncotermLoader {

    /**
     * Libellé FOB
     */
    public static final String FOB_LIB = "Franco bord";
    /**
     * Libellé DES
     */
    public static final String DES_LIB = "Rendu le long du navire";
    /**
     * Libellé FCA
     */
    public static final String FCA_LIB = "Franco transporteur";
    /**
     * Libellé FCA
     */
    public static final String FAS_LIB = "Free Alongside Ship";
    /**
     * Code FOB
     */
    public static final String FOB_CODE = "FOB";
    /**
     * Code DES
     */
    public static final String DES_CODE = "DES";
    /**
     * Code FCA
     */
    public static final String FCA_CODE = "FCA";
    /**
     * Code FCA
     */
    public static final String FAS_CODE = "FAS";
    /**
     * Motif effet FOB
     */
    public static final String FOB_MOTIF_EFFET = "Motif effet FOB";
    /**
     * Motif effet DES
     */
    public static final String DES_MOTIF_EFFET = "Motif effet DES";
    /**
     * Motif effet FCA
     */
    public static final String FCA_MOTIF_EFFET = "Motif effet FCA";
    /**
     * Motif effet FCA
     */
    public static final String FAS_MOTIF_EFFET = "Motif effet FAS";
    /**
     * Motif fin effet FOB
     */
    public static final String FOB_MOTIF_FIN_EFFET = "Motif fin effet FOB";
    /**
     * Motif fin effet DES
     */
    public static final String DES_MOTIF_FIN_EFFET = "Motif fin effet DES";
    /**
     * Motif fin effet FCA
     */
    public static final String FCA_MOTIF_FIN_EFFET = "Motif fin effet FCA";
    /**
     * Motif fin effet FCA
     */
    public static final String FAS_MOTIF_FIN_EFFET = "Motif fin effet FAS";

    private List<Incoterm> lIncoterms = new ArrayList<>();

    @Autowired
    IncotermRepository incotermRepo;

    /**
     * Création et enregistrement d'une Unité
     *
     * @param code Code
     * @param designation Désignation
     * @param dateEffet Date d'effet
     * @param motifEffet motif Date effet
     * @param dateFinEffet Date de fin d'effet
     * @param motifFinEffet motif Date fin effet
     * @return l'entité créée si elle n'existe pas déjà, sinon celle de la BDD
     */
    public Incoterm createIncoterm(final String code, final String designation, final LocalDate dateEffet,
            final String motifEffet, final LocalDate dateFinEffet, final String motifFinEffet) {

        // Vérifier si l'Unité existe déjà. Si oui, la réuitiliser
        Incoterm entt = incotermRepo.findByCodeAndDateEffet(code, dateEffet);

        if (entt == null) {
            entt = new Incoterm();
            entt.setCode(code);
            entt.setDesignation(designation);
            entt.setDateEffet(dateEffet);
            entt.setMotifEffet(motifEffet);
            entt.setDateFinEffet(dateFinEffet);
            entt.setMotifFinEffet(motifFinEffet);

            entt = incotermRepo.save(entt);
        }

        lIncoterms.add(entt);

        return entt;
    }

    /**
     * Chargement de données
     */
    public void loadData() {
        // FOB
        createIncoterm(FOB_CODE, FOB_LIB, LocalDate.of(2013, 1, 1), FOB_MOTIF_EFFET, LocalDate.of(2013, 12, 31), FOB_MOTIF_FIN_EFFET);
        createIncoterm(FOB_CODE, FOB_LIB, LocalDate.of(2014, 1, 1), FOB_MOTIF_EFFET, LocalDate.of(2014, 12, 31), FOB_MOTIF_FIN_EFFET);
        createIncoterm(FOB_CODE, FOB_LIB, LocalDate.of(2015, 1, 1), FOB_MOTIF_EFFET, LocalDate.of(2015, 12, 31), FOB_MOTIF_FIN_EFFET);
        createIncoterm(FOB_CODE, FOB_LIB, LocalDate.of(2016, 1, 1), FOB_MOTIF_EFFET, LocalDate.of(2016, 12, 31), FOB_MOTIF_FIN_EFFET);
        createIncoterm(FOB_CODE, FOB_LIB, LocalDate.of(2035, 1, 1), FOB_MOTIF_EFFET, null, null);

        // DES
        createIncoterm(DES_CODE, DES_LIB, LocalDate.of(2013, 1, 1), DES_MOTIF_EFFET, LocalDate.of(2013, 12, 31), DES_MOTIF_FIN_EFFET);
        createIncoterm(DES_CODE, DES_LIB, LocalDate.of(2014, 1, 1), DES_MOTIF_EFFET, LocalDate.of(2014, 12, 31), DES_MOTIF_FIN_EFFET);
        createIncoterm(DES_CODE, DES_LIB, LocalDate.of(2015, 1, 1), DES_MOTIF_EFFET, LocalDate.of(2015, 12, 31), DES_MOTIF_FIN_EFFET);
        createIncoterm(DES_CODE, DES_LIB, LocalDate.of(2016, 1, 1), DES_MOTIF_EFFET, LocalDate.of(2016, 12, 31), DES_MOTIF_FIN_EFFET);
        createIncoterm(DES_CODE, DES_LIB, LocalDate.of(2035, 1, 1), DES_MOTIF_EFFET, null, null);

        // FCA
        createIncoterm(FCA_CODE, FCA_LIB, LocalDate.of(2013, 1, 1), FCA_MOTIF_EFFET, LocalDate.of(2013, 12, 31), FCA_MOTIF_FIN_EFFET);
        createIncoterm(FCA_CODE, FCA_LIB, LocalDate.of(2014, 1, 1), FCA_MOTIF_EFFET, LocalDate.of(2014, 12, 31), FCA_MOTIF_FIN_EFFET);
        createIncoterm(FCA_CODE, FCA_LIB, LocalDate.of(2015, 1, 1), FCA_MOTIF_EFFET, LocalDate.of(2015, 12, 31), FCA_MOTIF_FIN_EFFET);
        createIncoterm(FCA_CODE, FCA_LIB, LocalDate.of(2016, 1, 1), FCA_MOTIF_EFFET, LocalDate.of(2016, 12, 31), FCA_MOTIF_FIN_EFFET);
        createIncoterm(FCA_CODE, FCA_LIB, LocalDate.of(2035, 1, 1), FCA_MOTIF_EFFET, null, null);

        // FAS
        createIncoterm(FAS_CODE, FAS_LIB, LocalDate.of(2013, 1, 1), FAS_MOTIF_EFFET, LocalDate.of(2013, 12, 31), FAS_MOTIF_FIN_EFFET);
        createIncoterm(FAS_CODE, FAS_LIB, LocalDate.of(2014, 1, 1), FAS_MOTIF_EFFET, LocalDate.of(2014, 12, 31), FAS_MOTIF_FIN_EFFET);
        createIncoterm(FAS_CODE, FAS_LIB, LocalDate.of(2015, 1, 1), FAS_MOTIF_EFFET, LocalDate.of(2015, 12, 31), FAS_MOTIF_FIN_EFFET);
        createIncoterm(FAS_CODE, FAS_LIB, LocalDate.of(2016, 1, 1), FAS_MOTIF_EFFET, LocalDate.of(2016, 12, 31), FAS_MOTIF_FIN_EFFET);
        createIncoterm(FAS_CODE, FAS_LIB, LocalDate.of(2035, 1, 1), FAS_MOTIF_EFFET, null, null);
    }

    /**
     * Suppression des données
     */
    public void removeData() {
        incotermRepo.delete(lIncoterms);
    }
}
