package nc.gouv.drdnc.ilda.metier.tarifs.catalogue.section;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import nc.gouv.drdnc.ilda.commons.historisable.IHistorisable;
import nc.gouv.drdnc.ilda.commons.historisable.motif.AbstractHistoAvecMotifEntity;

/**
 * Entité JPA de la table T_SECTION
 *
 * @author ILDA
 */
@Entity
@Table(name = "\"T_SECTION\"", schema = "\"ILDA\"")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "JavassistLazyInitializer" })
public class Section extends AbstractHistoAvecMotifEntity {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    @Column(name = "NUMERO_SECTION")
    private String numeroSection;

    @Override
    public boolean isUpdateChangesAllowed(IHistorisable aBddData) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * Retourne la valeur du champ numeroSection
     * 
     * @return {@link #numeroSection}
     */
    public String getNumeroSection() {
        return numeroSection;
    }

    /**
     * Renseigne la valeur du champ numeroSection
     * 
     * @param numeroSection valeur à setter pour {@link #numeroSection}
     */
    public void setNumeroSection(String numeroSection) {
        this.numeroSection = numeroSection;
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
        Section castOther = (Section) other;
        return Objects.equals(getNumeroSection(), castOther.getNumeroSection());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getNumeroSection());
    }

}
