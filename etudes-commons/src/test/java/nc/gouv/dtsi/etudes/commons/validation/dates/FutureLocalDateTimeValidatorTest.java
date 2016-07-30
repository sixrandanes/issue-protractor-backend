/**
 *
 */
package nc.gouv.dtsi.etudes.commons.validation.dates;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

import nc.gouv.dtsi.etudes.commons.utils.Constantes;
import nc.gouv.dtsi.etudes.commons.validation.dates.FutureLocalDateTimeValidator;

/**
 * Tests de la classe FutureLocalDateTimeValidator
 *
 * @author FFAIVE
 */
public class FutureLocalDateTimeValidatorTest {

    private FutureLocalDateTimeValidator validator;

    /**
     * Setup before tests
     */
    @Before
    public void setUp() {
        validator = new FutureLocalDateTimeValidator();
    }

    /**
     * Test method for {@link FutureLocalDateTimeValidator#isValid(java.time.LocalDateTime, javax.validation.ConstraintValidatorContext)} .
     */
    @Test
    public void testIsValid() {

        final LocalDateTime now = LocalDateTime.now(ZoneId.of(Constantes.GMT11));

        // Now : OK
        assertThat(validator.isValid(now.plus(100, ChronoUnit.MILLIS), null)).isTrue();
        // Future : OK
        assertThat(validator.isValid(now.plus(1, ChronoUnit.DAYS), null)).isTrue();
        // Past : KO
        assertThat(validator.isValid(now.minus(1, ChronoUnit.DAYS), null)).isFalse();
        // Null : OK
        assertThat(validator.isValid(null, null)).isTrue();
    }

}
