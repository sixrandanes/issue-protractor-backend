package nc.gouv.dtsi.etudes.commons.utils;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.hateoas.Identifiable;

/**
 * Interface des entités JPA, déclarant les méthodes d'historique
 *
 * @author ILDA
 */
public interface IEntity extends Serializable, Identifiable<Serializable> {

    /**
     * @return l'ID de l'entité
     */
    @Override
    Serializable getId();

    /**
     * @return Le numéro de version
     */
    Long getVersionNum();

    /**
     * @param aVersionNum valeur à setter pour le numéro de version
     */
    void setVersionNum(Long aVersionNum);

    /**
     * @return la date de création
     */
    LocalDateTime getDateCre();

    /**
     * @param aDateCre valeur à setter pour la date de création
     */
    void setDateCre(LocalDateTime aDateCre);

    /**
     * @return l'auteur de création
     */
    String getAuteurCre();

    /**
     * @param aAuteurCre valeur à setter pour l'auteur de création
     */
    void setAuteurCre(String aAuteurCre);

    /**
     * @return la date de mise à jour
     */
    LocalDateTime getDateMaj();

    /**
     * @param aDateMaj valeur à setter pour la date de mise à jour
     */
    void setDateMaj(LocalDateTime aDateMaj);

    /**
     * @return l'auteur de mise à jour
     */
    String getAuteurMaj();

    /**
     * @param aAuteurMaj valeur à setter pour l'auteur de mise à jour
     */
    void setAuteurMaj(String aAuteurMaj);

}
