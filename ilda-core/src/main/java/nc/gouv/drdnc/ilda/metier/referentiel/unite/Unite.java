package nc.gouv.drdnc.ilda.metier.referentiel.unite;

import static nc.gouv.drdnc.ilda.commons.utils.Messages.MANDATORY;
import static nc.gouv.drdnc.ilda.commons.utils.Messages.MAX_LENGTH_5;
import static nc.gouv.drdnc.ilda.commons.utils.Messages.MAX_LENGTH_50;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import nc.gouv.drdnc.ilda.commons.historisable.AbstractHistoEntity;
import nc.gouv.drdnc.ilda.commons.historisable.IHistorisable;
import nc.gouv.dtsi.etudes.commons.utils.Constantes;
import nc.gouv.dtsi.etudes.commons.validation.string.AllUpperCase;

/**
 * Entité JPA de la table T_UNITE
 *
 * @author ILDA
 */
@Entity
@Table(name = "\"T_UNITE\"", schema = "\"ILDA\"")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "JavassistLazyInitializer" })
public class Unite extends AbstractHistoEntity {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 5645392901610242954L;

    @NotNull(message = MANDATORY)
    @Size(max = 5, message = MAX_LENGTH_5)
    @Column(name = "CODE")
    @AllUpperCase
    private String code;

    @NotNull(message = MANDATORY)
    @Size(max = 50, message = MAX_LENGTH_50)
    @Column(name = "DESIGNATION")
    private String designation;

    /**
     * Retourne la valeur du champ code
     *
     * @return {@link #code}
     */
    public String getCode() {
        return code;
    }

    /**
     * Renseigne la valeur du champ code
     *
     * @param code valeur à setter pour {@link #code}
     */
    public void setCode(final String code) {
        this.code = code;
    }

    /**
     * Retourne la valeur du champ designation
     *
     * @return {@link #designation}
     */
    public String getDesignation() {
        return designation;
    }

    /**
     * Renseigne la valeur du champ designation
     *
     * @param designation valeur à setter pour {@link #designation}
     */
    public void setDesignation(final String designation) {
        this.designation = designation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object other) {
        if (other == null) {
            return false;
        }
        if (!getClass().equals(other.getClass())) {
            return false;
        }
        if (!super.equals(other)) {
            return false;
        }
        Unite castOther = (Unite) other;
        return Objects.equals(getCode(), castOther.getCode()) && Objects.equals(getDesignation(), castOther.getDesignation());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getCode(), getDesignation());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("Unite[code='%s', Désignation='%s', du %s au %s]", code, designation, getDateEffetString(), getDateFinEffetString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isUpdateChangesAllowed(final IHistorisable bddData) {
        Unite bddUnite = (Unite) bddData;
        final LocalDate now = LocalDate.now(ZoneId.of(Constantes.GMT11));

        // RG : la date de fin d'effet est modifiable tant qu'elle est vide ou postérieure à la date du jour
        boolean isDFECorrect = bddUnite.getDateFinEffet() == null || !now.isAfter(bddUnite.getDateFinEffet());

        // RG : seule la date de fin d'effet peut être modifiée (les autres champs doivent rester inchangés)
        boolean isChangeAllowed = Objects.equals(getCode(), bddUnite.getCode())
                && Objects.equals(getDesignation(), bddUnite.getDesignation())
                && Objects.equals(getDateEffet(), bddUnite.getDateEffet());

        return isDFECorrect && isChangeAllowed;
    }

}
