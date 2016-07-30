package nc.gouv.drdnc.ilda.metier.referentiel.tauxchange;

import static nc.gouv.drdnc.ilda.commons.utils.Messages.MANDATORY;
import static nc.gouv.drdnc.ilda.commons.utils.Messages.MAX_LENGTH_500;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.rest.core.annotation.RestResource;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import nc.gouv.drdnc.ilda.metier.referentiel.devise.Devise;
import nc.gouv.dtsi.etudes.commons.utils.AbstractUUIEntity;
import nc.gouv.dtsi.etudes.commons.utils.json.CurrencyJsonDeserializer;
import nc.gouv.dtsi.etudes.commons.utils.json.CurrencyJsonSerializer;
import nc.gouv.dtsi.etudes.commons.validation.dates.StrictFuture;

/**
 * Entité JPA de la table T_TAUX_CHANGE
 *
 * @author ILDA
 */
@Entity
@Table(name = "\"T_TAUX_CHANGE\"", schema = "\"ILDA\"")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "JavassistLazyInitializer" })

public class TauxChange extends AbstractUUIEntity {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -7118720723632214752L;

    @Digits(integer = 10, fraction = 5)
    @JsonSerialize(using = CurrencyJsonSerializer.class)
    @JsonDeserialize(using = CurrencyJsonDeserializer.class)
    @NotNull(message = MANDATORY)
    @Column(name = "VALEUR")
    private BigDecimal valeur;

    @Column(name = "DATE_EFFET")
    @StrictFuture
    @JsonFormat(pattern = "dd/MM/yyyy")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate dateEffet;

    @Size(max = 500, message = MAX_LENGTH_500)
    @Column(name = "MOTIF_EFFET")
    private String motifEffet;

    @RestResource(rel = "deviseTxChange")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_DEVISE", referencedColumnName = "ID")
    @NotNull
    private Devise devise;

    /**
     * Retourne la valeur du champ valeur
     * 
     * @return {@link #valeur}
     */
    public BigDecimal getValeur() {
        return valeur;
    }

    /**
     * Renseigne la valeur du champ valeur
     * 
     * @param valeur valeur à setter pour {@link #valeur}
     */
    public void setValeur(BigDecimal valeur) {
        this.valeur = valeur;
    }

    /**
     * Retourne la valeur du champ dateEffet
     * 
     * @return {@link #dateEffet}
     */
    public LocalDate getDateEffet() {
        return dateEffet;
    }

    /**
     * Renseigne la valeur du champ dateEffet
     * 
     * @param dateEffet valeur à setter pour {@link #dateEffet}
     */
    public void setDateEffet(LocalDate dateEffet) {
        this.dateEffet = dateEffet;
    }

    /**
     * Retourne la valeur du champ motifEffet
     * 
     * @return {@link #motifEffet}
     */
    public String getMotifEffet() {
        return motifEffet;
    }

    /**
     * Renseigne la valeur du champ motifEffet
     * 
     * @param motifEffet valeur à setter pour {@link #motifEffet}
     */
    public void setMotifEffet(String motifEffet) {
        this.motifEffet = motifEffet;
    }

    /**
     * Retourne la valeur du champ devise
     * 
     * @return {@link #devise}
     */
    public Devise getDevise() {
        return devise;
    }

    /**
     * Retourne la valeur du id de la devise (utilisé pour calculer le equals)
     * 
     * @return {@link #devise}
     */
    public UUID getDeviseId() {
        UUID result = null;
        if (devise != null) {
            result = devise.getId();
        }
        return result;
    }

    /**
     * Renseigne la valeur du champ devise
     * 
     * @param devise valeur à setter pour {@link #devise}
     */
    public void setDevise(Devise devise) {
        this.devise = devise;
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
        TauxChange castOther = (TauxChange) other;
        return Objects.equals(getValeur(), castOther.getValeur()) && Objects.equals(getDateEffet(), castOther.getDateEffet())
                && Objects.equals(getMotifEffet(), castOther.getMotifEffet()) && Objects.equals(getDeviseId(), castOther.getDeviseId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getValeur(), getDateEffet(), getMotifEffet(), getDeviseId());
    }

}
