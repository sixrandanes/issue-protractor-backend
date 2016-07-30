/**
 *
 */
package nc.gouv.drdnc.ilda.commons.historisable;

import static nc.gouv.dtsi.etudes.commons.utils.Constantes.DTF;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import nc.gouv.dtsi.etudes.commons.utils.AbstractUUIEntity;

/**
 * Factorisation des champs d'une entité avec historique (date d'effet et de fin d'effet)
 *
 * @author ILDA
 */
@MappedSuperclass
public abstract class AbstractHistoEntity extends AbstractUUIEntity implements IHistorisable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 150401042193202270L;

    @Column(name = "DATE_EFFET")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate dateEffet;

    @Column(name = "DATE_FIN_EFFET")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate dateFinEffet;

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDate getDateEffet() {
        return dateEffet;
    }

    /**
     * Renseigne la valeur du champ dateEffet
     *
     * @param dateEffet valeur à setter pour {@link #dateEffet}
     */
    public void setDateEffet(final LocalDate dateEffet) {
        this.dateEffet = dateEffet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDate getDateFinEffet() {
        return dateFinEffet;
    }

    /**
     * Renseigne la valeur du champ dateFinEffet
     *
     * @param dateFinEffet valeur à setter pour {@link #dateFinEffet}
     */
    public void setDateFinEffet(final LocalDate dateFinEffet) {
        this.dateFinEffet = dateFinEffet;
    }

    /**
     * @return La date d'effet, au format dd/MM/yyyy
     */
    protected String getDateEffetString() {
        // Le DateTimeFormatter n'accepte pas les valeurs nulles
        if (getDateEffet() != null) {
            return DTF.format(getDateEffet());
        }
        return "";
    }

    /**
     * @return La date de fin d'effet, au format dd/MM/yyyy
     */
    protected String getDateFinEffetString() {
        // Le DateTimeFormatter n'accepte pas les valeurs nulles
        if (getDateFinEffet() != null) {
            return DTF.format(getDateFinEffet());
        }
        return "";
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
        AbstractHistoEntity castOther = (AbstractHistoEntity) other;
        return Objects.equals(getDateEffet(), castOther.getDateEffet()) && Objects.equals(getDateFinEffet(), castOther.getDateFinEffet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getDateEffet(), getDateFinEffet());
    }
}
