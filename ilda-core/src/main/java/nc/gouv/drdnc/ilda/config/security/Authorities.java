package nc.gouv.drdnc.ilda.config.security;

/**
 * Interface regoupant l'ensemble des permissions disponibles et permettant d' appliquer les regles de securite
 *
 * @author ILDA
 */
public interface Authorities {

    /**
     * Constantes gestion des autorités
     */
    String ILDA_AUTHORITY = "AUT_ILD";

    /*
     * REFERENTIEL
     */
    String REFERENTIEL_CONSULTATION = "REF_REA";

    String REFERENTIEL_CREATION = "REF_NEW";

    String REFERENTIEL_MODIFICATION = "REF_UPD";

    String REFERENTIEL_SUPPRESSION = "REF_DEL";
}
