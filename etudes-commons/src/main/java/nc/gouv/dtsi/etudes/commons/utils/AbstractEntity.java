/**
 *
 */
package nc.gouv.dtsi.etudes.commons.utils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

/**
 * Super class des entités JPA, factorisant les champs d'historique
 *
 * @author ILDA
 */
@MappedSuperclass
public abstract class AbstractEntity implements IEntity {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3733578677349824907L;

    @Version
    @Column(name = "version_num")
    private Long versionNum;

    @Column(name = "date_creation")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime dateCre;

    @Column(name = "auteur_creation")
    private String auteurCre;

    @Column(name = "date_maj")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime dateMaj;

    @Column(name = "auteur_maj")
    private String auteurMaj;

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract Serializable getId();

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getVersionNum() {
        return versionNum;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setVersionNum(final Long aVersionNum) {
        versionNum = aVersionNum;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime getDateCre() {
        return dateCre;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDateCre(final LocalDateTime aDateCre) {
        dateCre = aDateCre;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAuteurCre() {
        return auteurCre;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAuteurCre(final String aAuteurCre) {
        auteurCre = aAuteurCre;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime getDateMaj() {
        return dateMaj;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDateMaj(final LocalDateTime aDateMaj) {
        dateMaj = aDateMaj;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAuteurMaj() {
        return auteurMaj;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAuteurMaj(final String aAuteurMaj) {
        auteurMaj = aAuteurMaj;
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
        final IEntity castOther = (IEntity) other;
        return Objects.equals(getVersionNum(), castOther.getVersionNum()) && Objects.equals(getDateCre(), castOther.getDateCre())
                && Objects.equals(getAuteurCre(), castOther.getAuteurCre()) && Objects.equals(getDateMaj(), castOther.getDateMaj())
                && Objects.equals(getAuteurMaj(), castOther.getAuteurMaj());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(getVersionNum(), getDateCre(), getAuteurCre(), getDateMaj(), getAuteurMaj());
    }

    /**
     * Récupère l'ID d'une IEntity pouvant être nulle
     *
     * @param entt IEntity, pouvant être nulle
     * @return son ID
     */
    protected static final Serializable getId(final IEntity entt) {
        if (entt == null) {
            return null;
        }
        return entt.getId();
    }
}
