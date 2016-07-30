package nc.gouv.drdnc.ilda.metier.referentiel.devise;

import static nc.gouv.drdnc.ilda.commons.utils.Messages.MANDATORY;
import static nc.gouv.drdnc.ilda.commons.utils.Messages.MAX_LENGTH_50;
import static nc.gouv.drdnc.ilda.commons.utils.Messages.SIZE_FIELD_3;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.rest.core.annotation.RestResource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import nc.gouv.drdnc.ilda.commons.historisable.AbstractHistoEntity;
import nc.gouv.drdnc.ilda.commons.historisable.IHistorisable;
import nc.gouv.drdnc.ilda.metier.referentiel.tauxchange.TauxChange;
import nc.gouv.dtsi.etudes.commons.utils.Constantes;
import nc.gouv.dtsi.etudes.commons.validation.string.AllUpperCase;

/**
 * Entité JPA de la table T_DEVISE
 *
 * @author ILDA
 */
@Entity
@Table(name = "\"T_DEVISE\"", schema = "\"ILDA\"")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "JavassistLazyInitializer" })
public class Devise extends AbstractHistoEntity {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 6583116658001916416L;

    @NotNull(message = MANDATORY)
    @Size(min = 3, max = 3, message = SIZE_FIELD_3)
    @Column(name = "CODE")
    @AllUpperCase
    private String code;

    @NotNull(message = MANDATORY)
    @Size(max = 50, message = MAX_LENGTH_50)
    @Column(name = "DESIGNATION")
    private String designation;

    @RestResource(exported = false)
    @OneToMany(mappedBy = "devise", cascade = CascadeType.REMOVE)
    private Set<TauxChange> tauxChanges;

    @Transient
    private BigDecimal tauxInitial;

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
     * Retourne la valeur du champ tauxChanges
     *
     * @return {@link #tauxChanges}
     */
    public Set<TauxChange> getTauxChanges() {
        return tauxChanges;
    }

    /**
     * Renseigne la valeur du champ tauxChanges
     *
     * @param tauxChanges valeur à setter pour {@link #tauxChanges}
     */
    public void setTauxChanges(final Set<TauxChange> tauxChanges) {
        this.tauxChanges = tauxChanges;
    }

    /**
     * Retourne la valeur du champ tauxInitial
     *
     * @return {@link #tauxInitial}
     */
    public BigDecimal getTauxInitial() {
        return tauxInitial;
    }

    /**
     * Renseigne la valeur du champ tauxInitial
     *
     * @param tauxInitial valeur à setter pour {@link #tauxInitial}
     */
    public void setTauxInitial(final BigDecimal tauxInitial) {
        this.tauxInitial = tauxInitial;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isUpdateChangesAllowed(final IHistorisable bddData) {
        Devise devise = (Devise) bddData;

        // RG : Est modifiable tant qu'elle est vide ou postérieure à la date du jour
        final LocalDate now = LocalDate.now(ZoneId.of(Constantes.GMT11));
        boolean isDFECorrect = devise.getDateFinEffet() == null || !now.isAfter(devise.getDateFinEffet());

        // RG : seule la date de fin d'effet peut être modifiée (les autres champs doivent rester inchangés)
        boolean isChangeAllowed = Objects.equals(getCode(), devise.getCode())
                && Objects.equals(getDesignation(), devise.getDesignation())
                && Objects.equals(getDateEffet(), devise.getDateEffet());

        return isDFECorrect && isChangeAllowed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (!getClass().equals(other.getClass())) {
            return false;
        }
        if (!super.equals(other)) {
            return false;
        }
        Devise castOther = (Devise) other;
        return Objects.equals(getCode(), castOther.getCode()) && Objects.equals(getDesignation(), castOther.getDesignation())
                && Objects.equals(getTauxChanges(), castOther.getTauxChanges());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getCode(), getDesignation(), getTauxChanges());
    }

}
