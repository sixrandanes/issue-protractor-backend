/**
 *
 */
package nc.gouv.drdnc.ilda.commons.historisable.simple;

import static nc.gouv.drdnc.ilda.commons.utils.Constantes.DTF;
import static nc.gouv.drdnc.ilda.commons.utils.Messages.MANDATORY;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import nc.gouv.drdnc.ilda.commons.utils.AbstractUUIEntity;


/**
 * Factorisation des champs d'une entité avec historique (date d'effet uniquement)
 *
 * @author ILDA
 */
@MappedSuperclass
public abstract class AbstractSimpleHistoEntity extends AbstractUUIEntity implements ISimpleHistorisable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3834688357220715598L;

    @Column(name = "DATE_EFFET")
    @NotNull(message = MANDATORY)
    // @StrictFuture // Dans les BeforeCreate et BeforeUpdate pour les TI
    @JsonFormat(pattern = "dd/MM/yyyy")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate dateEffet;

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
        AbstractSimpleHistoEntity castOther = (AbstractSimpleHistoEntity) other;
        return Objects.equals(getDateEffet(), castOther.getDateEffet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getDateEffet());
    }
}
