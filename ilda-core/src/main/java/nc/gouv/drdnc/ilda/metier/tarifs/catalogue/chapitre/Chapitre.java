package nc.gouv.drdnc.ilda.metier.tarifs.catalogue.chapitre;

import static nc.gouv.drdnc.ilda.commons.utils.Messages.MANDATORY;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import org.springframework.data.rest.core.annotation.RestResource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import nc.gouv.drdnc.ilda.commons.historisable.IHistorisable;
import nc.gouv.drdnc.ilda.commons.historisable.motif.AbstractHistoAvecMotifEntity;
import nc.gouv.drdnc.ilda.metier.tarifs.catalogue.libellechapitre.LibelleChapitre;
import nc.gouv.drdnc.ilda.metier.tarifs.catalogue.section.Section;

/**
 * Entité JPA de la table T_CHAPITRE
 *
 * @author ILDA
 */
@Entity
@Table(name = "\"T_CHAPITRE\"", schema = "\"ILDA\"")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "JavassistLazyInitializer" })
public class Chapitre extends AbstractHistoAvecMotifEntity {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 314483838472220399L;

    @NotNull(message = MANDATORY)
    @Max(99)
    @Column(name = "NUMERO_CHAPITRE")
    private Integer numeroChapitre;

    @RestResource(rel = "sectionChapitre")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SECTION", referencedColumnName = "ID")
    @NotNull
    private Section section;

    @RestResource(exported = false)
    @OneToMany(mappedBy = "chapitre", cascade = CascadeType.REMOVE)
    private Set<LibelleChapitre> libellesChapitres;

    @Transient
    private String designationLibelleDefaut;

    @Transient
    private String notesLibelleDefaut;

    /**
     * Retourne la valeur du champ numeroChapitre
     * 
     * @return {@link #numeroChapitre}
     */
    public Integer getNumeroChapitre() {
        return numeroChapitre;
    }

    /**
     * Renseigne la valeur du champ numeroChapitre
     * 
     * @param numeroChapitre valeur à setter pour {@link #numeroChapitre}
     */
    public void setNumeroChapitre(Integer numeroChapitre) {
        this.numeroChapitre = numeroChapitre;
    }

    /**
     * Retourne la valeur du champ libellesChapitres
     * 
     * @return {@link #libellesChapitres}
     */
    public Set<LibelleChapitre> getLibellesChapitres() {
        return libellesChapitres;
    }

    /**
     * Renseigne la valeur du champ libellesChapitres
     * 
     * @param libellesChapitres valeur à setter pour {@link #libellesChapitres}
     */
    public void setLibellesChapitres(Set<LibelleChapitre> libellesChapitres) {
        this.libellesChapitres = libellesChapitres;
    }

    /**
     * Retourne la valeur du champ section
     * 
     * @return {@link #section}
     */
    public Section getSection() {
        return section;
    }

    /**
     * Renseigne la valeur du champ section
     * 
     * @param section valeur à setter pour {@link #section}
     */
    public void setSection(Section section) {
        this.section = section;
    }

    /**
     * Retourne la valeur du id de la section (utilisé pour calculer le equals)
     * 
     * @return {@link #section}
     */
    public UUID getSectionId() {
        UUID result = null;
        if (section != null) {
            result = section.getId();
        }
        return result;
    }

    /**
     * Retourne la valeur du champ designationLibelleDefaut
     * 
     * @return {@link #designationLibelleDefaut}
     */
    public String getDesignationLibelleDefaut() {
        return designationLibelleDefaut;
    }

    /**
     * Renseigne la valeur du champ designationLibelleDefaut
     * 
     * @param designationLibelleDefaut valeur à setter pour {@link #designationLibelleDefaut}
     */
    public void setDesignationLibelleDefaut(String designationLibelleDefaut) {
        this.designationLibelleDefaut = designationLibelleDefaut;
    }

    /**
     * Retourne la valeur du champ notesLibelleDefaut
     * 
     * @return {@link #notesLibelleDefaut}
     */
    public String getNotesLibelleDefaut() {
        return notesLibelleDefaut;
    }

    /**
     * Renseigne la valeur du champ notesLibelleDefaut
     * 
     * @param notesLibelleDefaut valeur à setter pour {@link #notesLibelleDefaut}
     */
    public void setNotesLibelleDefaut(String notesLibelleDefaut) {
        this.notesLibelleDefaut = notesLibelleDefaut;
    }

    @Override
    public boolean isUpdateChangesAllowed(IHistorisable aBddData) {
        // TODO Auto-generated method stub
        return false;
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
        Chapitre castOther = (Chapitre) other;
        return Objects.equals(getNumeroChapitre(), castOther.getNumeroChapitre()) && Objects.equals(getSectionId(), castOther.getSectionId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getNumeroChapitre(), getSectionId());
    }

}
