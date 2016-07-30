package nc.gouv.dtsi.etudes.commons.validation.dates;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * La date annotée doit être strictement inférieure à la date du jour.
 * <p/>
 * Types supportés :
 * <ul>
 * <li>{@code java.time.LocalDate}</li>
 * <li>{@code java.time.LocalDateTime}</li>
 * </ul>
 *
 * @author FFAIVE
 */
@Constraint(validatedBy = { StrictPastLocalDateValidator.class, StrictPastLocalDateTimeValidator.class })
@Target(FIELD)
@Retention(RUNTIME)
public @interface StrictPast {

    /**
     * Message par défaut en cas d'erreur
     *
     * @return message par défaut
     */
    String message() default "{commons.validation.constraints.strict.past.message}";

    /**
     * @return group of class
     */
    Class<?>[] groups() default {};

    /**
     * @return group of class of type Payload
     */
    Class<? extends Payload>[] payload() default {};
}
