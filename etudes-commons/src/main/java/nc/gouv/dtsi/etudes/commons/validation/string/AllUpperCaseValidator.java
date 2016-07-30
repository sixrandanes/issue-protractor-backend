/**
 *
 */
package nc.gouv.dtsi.etudes.commons.validation.string;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validation d'un champ texte
 * <p>
 * RG : doit être en majuscules
 * </p>
 *
 * @author ILDA
 */
public class AllUpperCaseValidator implements ConstraintValidator<AllUpperCase, String> {

    /** Default error message */
    public static final String MESSAGE = "error.alluppercase";

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(final AllUpperCase constraint) {
        // empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(final String aValue, final ConstraintValidatorContext aContext) {
        if (aValue == null) {
            return true;
        }

        return aValue.chars()
                // Transformer le Stream<Int> en Stream<Char>
                .mapToObj(i -> (char) i)
                // Vérifier que tous les caractères sont des majuscules
                .allMatch(c -> !Character.isLetter(c) || !Character.isLowerCase(c));
    }
}
