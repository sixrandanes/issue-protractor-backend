package nc.gouv.drdnc.ilda.commons.historisable;

import nc.gouv.drdnc.ilda.commons.utils.IEntity;

import java.time.LocalDate;


/**
 * Interface représentant un objet ayant un historique, c'est-à-dire des plages de validité.
 *
 * @author ILDA
 */
public interface IHistorisable extends IEntity {

    /**
     * Retourne la valeur du champ dateEffet
     *
     * @return la date d'effet
     */
    LocalDate getDateEffet();

    /**
     * Retourne la valeur du champ dateFinEffet
     *
     * @return la date de fin d'effet
     */
    LocalDate getDateFinEffet();

    /**
     * Compare l'entité avec les données en base de données et vérifie si les changements faits sont cohérents avec les RG relatives à la mise à jour.
     * 
     * @param aBddData Données issues de la BDD, avant changements
     * @return true si les changements sont autorisés, false sinon.
     */
    boolean isUpdateChangesAllowed(IHistorisable aBddData);

}
