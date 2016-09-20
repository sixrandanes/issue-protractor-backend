package nc.gouv.drdnc.ilda.commons.validation.dates;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * La date annotée doit être supérieure ou égale à la date du jour.
 * <p/>
 * Types supportés :
 * <ul>
 * <li>{@code java.time.LocalDate}</li>
 * <li>{@code java.time.LocalDateTime}</li>
 * </ul>
 *
 * @author FFAIVE
 */
@Constraint(validatedBy = { FutureLocalDateValidator.class, FutureLocalDateTimeValidator.class })
@Target(FIELD)
@Retention(RUNTIME)
public @interface Future {

    /**
     * Message par défaut en cas d'erreur
     *
     * @return message par défaut
     */
    String message() default "{commons.validation.constraints.future.message}";

    /**
     * @return group of class
     */
    Class<?>[] groups() default {};

    /**
     * @return group of class of type Payload
     */
    Class<? extends Payload>[] payload() default {};
}
