package nc.gouv.dtsi.etudes.commons.validation.dates;

import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import nc.gouv.dtsi.etudes.commons.utils.Constantes;

/**
 * Validation des dates
 *
 * RG : La date doit être strictement inférieure à la date du jour
 *
 * @author FFAIVE
 */
public class StrictPastLocalDateTimeValidator implements ConstraintValidator<StrictPast, LocalDateTime> {

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
    public boolean isValid(final LocalDateTime aValue, final ConstraintValidatorContext aContext) {
        final LocalDateTime now = LocalDateTime.now(ZoneId.of(Constantes.GMT11));
        return (aValue == null) || (now.compareTo(aValue) > 0);
    }
}
