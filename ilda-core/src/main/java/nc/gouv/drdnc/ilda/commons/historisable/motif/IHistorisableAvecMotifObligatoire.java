package nc.gouv.drdnc.ilda.commons.historisable.motif;

import nc.gouv.drdnc.ilda.commons.historisable.IHistorisable;

/**
 * Interface représentant un objet ayant un historique, c'est-à-dire des plages de validité, avec motifs.
 *
 * @author ILDA
 */
public interface IHistorisableAvecMotifObligatoire extends IHistorisable {

    /**
     * Retourne la valeur du champ motifEffet
     *
     * @return le motif d'effet
     */
    String getMotifEffet();

    /**
     * Retourne la valeur du champ motifFinEffet
     *
     * @return le motif fin d'effet
     */
    String getMotifFinEffet();
}
