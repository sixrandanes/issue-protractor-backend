package nc.gouv.drdnc.ilda.commons.validation.dates;

import nc.gouv.drdnc.ilda.commons.utils.Constantes;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * Validation des dates
 *
 * RG : La date doit être strictement inférieure à la date du jour
 *
 * @author FFAIVE
 */
public class StrictPastLocalDateValidator implements ConstraintValidator<StrictPast, LocalDate> {

    /** Default error message */
    public static final String message = "La date doit être strictement inférieure à la date du jour";

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(final StrictPast constraint) {
        // empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(final LocalDate aValue, final ConstraintValidatorContext aContext) {
        final LocalDate now = LocalDate.now(ZoneId.of(Constantes.GMT11));
        return (aValue == null) || (now.compareTo(aValue) > 0);
    }
}
