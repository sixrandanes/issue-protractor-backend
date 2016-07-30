package nc.gouv.dtsi.etudes.commons.validation.string;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;


/**
 * Tests unitaires de la classe AllUpperCaseValidator
 *
 * @author ILDA
 */
public class AllUpperCaseValidatorTest {

    private AllUpperCaseValidator validator;

    /**
     * Setup before tests
     */
    @Before
    public void setUp() {
        validator = new AllUpperCaseValidator();
    }

    /**
     * Test method for {@link AllUpperCaseValidator#isValid(String, javax.validation.ConstraintValidatorContext)}.
     */
    @Test
    public void testIsValid() {

        // Majuscules : OK
        assertThat(validator.isValid("AZERTY", null)).isTrue();
        // Chiffres : OK
        assertThat(validator.isValid("0123456789", null)).isTrue();
        // Caractères spéciaux : OK
        assertThat(validator.isValid("/[]{}()#-$€@", null)).isTrue();
        // Combinés : OK
        assertThat(validator.isValid("AF-42@AF-84", null)).isTrue();
        // Null : OK
        assertThat(validator.isValid(null, null)).isTrue();

        // Minuscules : KO
        assertThat(validator.isValid("AZeRTY", null)).isFalse();
    }
}