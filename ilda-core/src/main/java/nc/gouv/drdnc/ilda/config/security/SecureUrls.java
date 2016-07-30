package nc.gouv.drdnc.ilda.config.security;

/**
 * Interface regroupant la liste des urls à sécuriser
 *
 * @author ILDA
 */
public interface SecureUrls {

    /**
     * Constantes gestion des URL securisées
     */
    String URL_REFERENTIEL = "/api/unites(.*)|/api/pays(.*)|/api/devises(.*)|/api/incoterms(.*)";
}
