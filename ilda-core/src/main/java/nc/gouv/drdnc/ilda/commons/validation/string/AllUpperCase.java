package nc.gouv.drdnc.ilda.commons.validation.string;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validation : le champ annoté doit être en majuscule
 *
 * @author ILDA
 */
@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = AllUpperCaseValidator.class)
public @interface AllUpperCase {

    /**
     * Message par défaut en cas d'erreur
     *
     * @return message par défaut
     */
    String message() default AllUpperCaseValidator.MESSAGE;

    /**
     * @return group of class
     */
    Class<?>[] groups() default {};

    /**
     * @return group of class of type Payload
     */
    Class<? extends Payload>[] payload() default {};
}
