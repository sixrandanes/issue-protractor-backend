/**
 *
 */
package nc.gouv.drdnc.ilda.commons.historisable.motif;

import static nc.gouv.drdnc.ilda.commons.utils.Messages.MAX_LENGTH_500;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Size;

import nc.gouv.drdnc.ilda.commons.historisable.AbstractHistoEntity;

/**
 * Factorisation des champs d'une entité avec motif d'historique (motif d'effet et de fin d'effet)
 *
 * @author ILDA
 */
@MappedSuperclass
public abstract class AbstractHistoAvecMotifEntity extends AbstractHistoEntity implements IHistorisableAvecMotif {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1722473163686247168L;

    @Column(name = "MOTIF_EFFET")
    @Size(max = 500, message = MAX_LENGTH_500)
    private String motifEffet;

    @Column(name = "MOTIF_FIN_EFFET")
    @Size(max = 500, message = MAX_LENGTH_500)
    private String motifFinEffet;

    /**
     * Retourne la valeur du champ motifEffet
     *
     * @return {@link #motifEffet}
     */
    @Override
    public String getMotifEffet() {
        return motifEffet;
    }

    /**
     * Renseigne la valeur du champ motifEffet
     *
     * @param motifEffet valeur à setter pour {@link #motifEffet}
     */
    public void setMotifEffet(final String motifEffet) {
        this.motifEffet = motifEffet;
    }

    /**
     * Retourne la valeur du champ motifFinEffet
     *
     * @return {@link #motifFinEffet}
     */
    @Override
    public String getMotifFinEffet() {
        return motifFinEffet;
    }

    /**
     * Renseigne la valeur du champ motifFinEffet
     *
     * @param motifFinEffet valeur à setter pour {@link #motifFinEffet}
     */
    public void setMotifFinEffet(final String motifFinEffet) {
        this.motifFinEffet = motifFinEffet;
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
        AbstractHistoAvecMotifEntity castOther = (AbstractHistoAvecMotifEntity) other;
        return Objects.equals(getMotifEffet(), castOther.getMotifEffet()) && Objects.equals(getMotifFinEffet(), castOther.getMotifFinEffet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getMotifEffet(), getMotifFinEffet());
    }

}
