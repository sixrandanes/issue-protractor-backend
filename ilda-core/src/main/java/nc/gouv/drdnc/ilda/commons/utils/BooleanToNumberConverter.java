package nc.gouv.drdnc.ilda.commons.utils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Converter pour enregistrer un number pour les boolean
 *
 * @author ILDA
 */
@Converter
public class BooleanToNumberConverter implements AttributeConverter<Boolean, Long> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Long convertToDatabaseColumn(final Boolean value) {
        if (Boolean.TRUE.equals(value)) {
            return Long.valueOf(1);
        }
        return Long.valueOf(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean convertToEntityAttribute(final Long value) {
        return Long.valueOf(1)
                .equals(value);
    }
}
