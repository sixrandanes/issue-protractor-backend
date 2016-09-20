package nc.gouv.drdnc.ilda.commons.validation.dates;

import nc.gouv.drdnc.ilda.commons.utils.Constantes;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * Validation des dates
 *
 * RG : La date doit être inférieure ou égale à la date du jour
 *
 * @author FFAIVE
 */
public class PastLocalDateValidator implements ConstraintValidator<Past, LocalDate> {

    /** Default error message */
    public static final String message = "La date doit être inférieure ou égale à la date du jour";

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(final Past constraint) {
        // empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(final LocalDate aValue, final ConstraintValidatorContext aContext) {
        final LocalDate now = LocalDate.now(ZoneId.of(Constantes.GMT11));
        return (aValue == null) || (now.compareTo(aValue) >= 0);
    }
}
