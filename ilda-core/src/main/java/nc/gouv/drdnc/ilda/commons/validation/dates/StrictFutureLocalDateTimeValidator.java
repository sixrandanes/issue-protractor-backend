package nc.gouv.drdnc.ilda.commons.validation.dates;

import nc.gouv.drdnc.ilda.commons.utils.Constantes;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Validation des dates
 *
 * RG : La date doit être strictement supérieure à la date du jour
 *
 * @author FFAIVE
 */
public class StrictFutureLocalDateTimeValidator implements ConstraintValidator<StrictFuture, LocalDateTime> {

    /** Default error message */
    public static final String message = "La date doit être strictement supérieure à la date du jour";

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(final StrictFuture constraint) {
        // empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(final LocalDateTime aValue, final ConstraintValidatorContext aContext) {
        final LocalDateTime now = LocalDateTime.now(ZoneId.of(Constantes.GMT11));
        return (aValue == null) || (now.compareTo(aValue) < 0);
    }
}
