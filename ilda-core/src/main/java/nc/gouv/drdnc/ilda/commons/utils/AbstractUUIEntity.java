package nc.gouv.drdnc.ilda.commons.utils;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Super class des entites JPA avec UUID comme identifiant
 * 
 * @author ILDA
 */
@MappedSuperclass
public abstract class AbstractUUIEntity extends AbstractEntity {

    /**
     * serialVersionUID auto generated
     */
    private static final long serialVersionUID = -3322682348127497867L;
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "ID")
    private UUID id;

    /**
     * @return {@link #id}
     */
    @Override
    public UUID getId() {
        return id;
    }

    /**
     * @param id valeur à setter pour {@link #id}
     */
    public void setId(UUID id) {
        this.id = id;
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
        AbstractUUIEntity castOther = (AbstractUUIEntity) other;
        return Objects.equals(getId(), castOther.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId());
    }

    /**
     * Retourne l'ID (toString()) sous forme de Optional<String>
     * 
     * @return string de l'id
     */
    public Optional<String> getIdString() {
        if (id != null) {
            return Optional.of(id.toString());
        }
        return Optional.empty();
    }
}
