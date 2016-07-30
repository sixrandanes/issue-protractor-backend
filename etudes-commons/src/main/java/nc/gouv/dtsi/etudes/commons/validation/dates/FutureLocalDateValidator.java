package nc.gouv.dtsi.etudes.commons.validation.dates;

import java.time.LocalDate;
import java.time.ZoneId;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import nc.gouv.dtsi.etudes.commons.utils.Constantes;

/**
 * Validation des dates
 *
 * RG : La date doit être supérieure ou égale à la date du jour
 *
 * @author FFAIVE
 */
public class FutureLocalDateValidator implements ConstraintValidator<Future, LocalDate> {

    /** Default error message */
    public static final String message = "La date doit être supérieure ou égale à la date du jour";

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(final Future constraint) {
        // empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(final LocalDate aValue, final ConstraintValidatorContext aContext) {
        final LocalDate now = LocalDate.now(ZoneId.of(Constantes.GMT11));
        return (aValue == null) || (now.compareTo(aValue) <= 0);
    }
}
