package nc.gouv.drdnc.ilda.metier.referentiel.incoterm;

import static nc.gouv.drdnc.ilda.commons.utils.Messages.LENGTH_3;
import static nc.gouv.drdnc.ilda.commons.utils.Messages.MANDATORY;
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

import nc.gouv.drdnc.ilda.commons.historisable.IHistorisable;
import nc.gouv.drdnc.ilda.commons.historisable.motif.AbstractHistoAvecMotifEntity;
import nc.gouv.dtsi.etudes.commons.utils.Constantes;
import nc.gouv.dtsi.etudes.commons.validation.string.AllUpperCase;

/**
 * Entité JPA de la table T_INCOTERM
 *
 * @author ILDA
 */
@Entity
@Table(name = "\"T_INCOTERM\"", schema = "\"ILDA\"")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "JavassistLazyInitializer" })
public class Incoterm extends AbstractHistoAvecMotifEntity {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 151069783616779395L;

    @NotNull(message = MANDATORY)
    @Size(min = 3, max = 3, message = LENGTH_3)
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
        this.code = code != null ? code.trim() : code;
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
        Incoterm castOther = (Incoterm) other;
        return Objects.equals(getCode(), castOther.getCode())
                && Objects.equals(getDesignation(), castOther.getDesignation());
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
        return String.format(
                "Incoterm[code='%s', Désignation='%s', du %s au %s, motif d'effet='%s', motif de fin d'effet='%s']",
                code, designation, getDateEffetString(), getDateFinEffetString(), getMotifEffet(), getMotifFinEffet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isUpdateChangesAllowed(final IHistorisable bddData) {
        Incoterm bddIncoterm = (Incoterm) bddData;
        final LocalDate now = LocalDate.now(ZoneId.of(Constantes.GMT11));

        // RG : la date et motif de fin d'effet est modifiable tant qu'elle est
        // vide ou postérieure à la date du jour
        boolean isDFECorrect = bddIncoterm.getDateFinEffet() == null || !now.isAfter(bddIncoterm.getDateFinEffet());

        // RG : seule la date de fin d'effet peut être modifiée (les autres
        // champs doivent rester inchangés)
        boolean isChangeAllowed = Objects.equals(getCode(), bddIncoterm.getCode())
                && Objects.equals(getDesignation(), bddIncoterm.getDesignation())
                && Objects.equals(getDateEffet(), bddIncoterm.getDateEffet())
                && Objects.equals(getMotifEffet(), bddIncoterm.getMotifEffet());

        return isDFECorrect && isChangeAllowed;
    }

}
