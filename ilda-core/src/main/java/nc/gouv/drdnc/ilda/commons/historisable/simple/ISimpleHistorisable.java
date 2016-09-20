package nc.gouv.drdnc.ilda.commons.historisable.simple;

import java.time.LocalDate;

import nc.gouv.drdnc.ilda.commons.historisable.IHistorisable;
import nc.gouv.drdnc.ilda.commons.utils.IEntity;

/**
 * Interface représentant un objet ayant un historique, c'est-à-dire des plages de validité.
 *
 * @author ILDA
 */
public interface ISimpleHistorisable extends IEntity {

    /**
     * Retourne la valeur du champ dateEffet
     *
     * @return la date d'effet
     */
    LocalDate getDateEffet();

    /**
     * retourne l'entité parente de l'objet courant
     *
     * @return l'entité parente
     */
    IHistorisable getParent();

}
