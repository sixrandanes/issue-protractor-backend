package nc.gouv.drdnc.ilda.metier.referentiel.pays;

import static nc.gouv.drdnc.ilda.commons.utils.Messages.MANDATORY;
import static nc.gouv.drdnc.ilda.commons.utils.Messages.MAX_LENGTH_50;
import static nc.gouv.drdnc.ilda.commons.utils.Messages.SIZE_FIELD_2;
import static nc.gouv.drdnc.ilda.commons.utils.Messages.SIZE_FIELD_3;

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
 * Entité JPA de la table T_PAYS
 *
 * @author ILDA
 */
@Entity
@Table(name = "\"T_PAYS\"", schema = "\"ILDA\"")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "JavassistLazyInitializer" })
public class Pays extends AbstractHistoEntity {

    /**
     * serialVersionUID
     */
	private static final long serialVersionUID = 7258889354040262114L;

	@NotNull(message = MANDATORY)
	@Size(min = 2, max = 2, message = SIZE_FIELD_2)
	@Column(name = "CODE2")
	@AllUpperCase
	private String code2;

    @NotNull(message = MANDATORY)
	@Size(min = 3, max = 3, message = SIZE_FIELD_3)
	@Column(name = "CODE3")
    @AllUpperCase
	private String code3;

    @NotNull(message = MANDATORY)
    @Size(max = 50, message = MAX_LENGTH_50)
    @Column(name = "DESIGNATION")
    private String designation;

    /**
	 * Retourne la valeur du champ code2
	 *
	 * @return {@link #code2}
	 */
	public String getCode2() {
		return code2;
	}

	/**
	 * Renseigne la valeur du champ code2
	 *
	 * @param code
	 *            valeur à setter pour {@link #code2}
	 */
	public void setCode2(final String code2) {
		this.code2 = code2;
	}

	/**
	 * Retourne la valeur du champ code3
	 *
	 * @return {@link #code3}
	 */
	public String getCode3() {
		return code3;
    }

    /**
	 * Renseigne la valeur du champ code3
	 *
	 * @param code
	 *            valeur à setter pour {@link #code3}
	 */
	public void setCode3(final String code3) {
		this.code3 = code3;
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
		Pays castOther = (Pays) other;
		return Objects.equals(getCode2(), castOther.getCode2()) && Objects.equals(getCode3(), castOther.getCode3())
				&& Objects.equals(getDesignation(), castOther.getDesignation());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
		return Objects.hash(super.hashCode(), getCode2(), getCode3(), getDesignation());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
		return String.format("Pays[code2='%s', code3='%s', Désignation='%s', du %s au %s]", code2, code3, designation,
				getDateEffetString(), getDateFinEffetString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isUpdateChangesAllowed(final IHistorisable bddData) {
		Pays bddPays = (Pays) bddData;
        final LocalDate now = LocalDate.now(ZoneId.of(Constantes.GMT11));

        // RG : la date de fin d'effet est modifiable tant qu'elle est vide ou postérieure à la date du jour
		boolean isDFECorrect = bddPays.getDateFinEffet() == null || !now.isAfter(bddPays.getDateFinEffet());

        // RG : seule la date de fin d'effet peut être modifiée (les autres champs doivent rester inchangés)
		boolean isChangeAllowed = Objects.equals(getCode2(), bddPays.getCode2())
				&& Objects.equals(getCode3(), bddPays.getCode3())
				&& Objects.equals(getDesignation(), bddPays.getDesignation())
				&& Objects.equals(getDateEffet(), bddPays.getDateEffet());

        return isDFECorrect && isChangeAllowed;
    }

}
