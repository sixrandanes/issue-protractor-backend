package nc.gouv.drdnc.ilda.metier.tarifs.catalogue.libellechapitre;

import static nc.gouv.drdnc.ilda.commons.utils.Messages.MANDATORY;
import static nc.gouv.drdnc.ilda.commons.utils.Messages.MAX_LENGTH_500;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.rest.core.annotation.RestResource;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import nc.gouv.drdnc.ilda.metier.tarifs.catalogue.chapitre.Chapitre;
import nc.gouv.dtsi.etudes.commons.utils.AbstractUUIEntity;
import nc.gouv.dtsi.etudes.commons.validation.dates.StrictFuture;

/**
 * Entité JPA de la table T_LIBELLE_CHAPITRE
 *
 * @author ILDA
 */
@Entity
@Table(name = "\"T_LIBELLE_CHAPITRE\"", schema = "\"ILDA\"")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "JavassistLazyInitializer" })
public class LibelleChapitre extends AbstractUUIEntity {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 7486227162633963271L;

    @NotNull(message = MANDATORY)
    @Size(max = 500, message = MAX_LENGTH_500)
    @Column(name = "DESIGNATION")
    private String designation;

    @Column(name = "NOTES")
    private String notes;

    @Column(name = "DATE_EFFET")
    @StrictFuture
    @JsonFormat(pattern = "dd/MM/yyyy")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate dateEffet;

    @Size(max = 500, message = MAX_LENGTH_500)
    @Column(name = "MOTIF_EFFET")
    private String motifEffet;

    @RestResource(rel = "chapitreLibelleChapitre")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CHAPITRE", referencedColumnName = "ID")
    @NotNull
    private Chapitre chapitre;

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
    public void setDesignation(String designation) {
        this.designation = designation;
    }

    /**
     * Retourne la valeur du champ notes
     * 
     * @return {@link #notes}
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Renseigne la valeur du champ notes
     * 
     * @param notes valeur à setter pour {@link #notes}
     */
    public void setNotes(String notes) {
        this.notes = notes;
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
     * Retourne la valeur du champ chapitre
     * 
     * @return {@link #chapitre}
     */
    public Chapitre getChapitre() {
        return chapitre;
    }

    /**
     * Renseigne la valeur du champ chapitre
     * 
     * @param chapitre valeur à setter pour {@link #chapitre}
     */
    public void setChapitre(Chapitre chapitre) {
        this.chapitre = chapitre;
    }

    /**
     * Retourne la valeur du id du chapitre (utilisé pour calculer le equals)
     * 
     * @return {@link #chapitre}
     */
    public UUID getChapitreId() {
        UUID result = null;
        if (chapitre != null) {
            result = chapitre.getId();
        }
        return result;
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
        LibelleChapitre castOther = (LibelleChapitre) other;
        return Objects.equals(getDesignation(), castOther.getDesignation()) && Objects.equals(getNotes(), castOther.getNotes())
                && Objects.equals(getDateEffet(), castOther.getDateEffet()) && Objects.equals(getMotifEffet(), castOther.getMotifEffet())
                && Objects.equals(getChapitreId(), castOther.getChapitreId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getDesignation(), getNotes(), getDateEffet(), getMotifEffet(), getChapitreId());
    }

}
