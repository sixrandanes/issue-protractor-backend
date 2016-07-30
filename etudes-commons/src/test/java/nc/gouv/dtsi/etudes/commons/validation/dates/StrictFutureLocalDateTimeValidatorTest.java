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
import nc.gouv.dtsi.etudes.commons.validation.dates.StrictFutureLocalDateTimeValidator;

/**
 * Tests de la classe StrictFutureLocalDateTimeValidator
 *
 * @author FFAIVE
 */
public class StrictFutureLocalDateTimeValidatorTest {

    private StrictFutureLocalDateTimeValidator validator;

    /**
     * Setup before tests
     */
    @Before
    public void setUp() {
        validator = new StrictFutureLocalDateTimeValidator();
    }

    /**
     * Test method for {@link StrictFutureLocalDateTimeValidator#isValid(java.time.LocalDateTime, javax.validation.ConstraintValidatorContext)} .
     */
    @Test
    public void testIsValid() {

        final LocalDateTime now = LocalDateTime.now(ZoneId.of(Constantes.GMT11));

        // Now : KO
        assertThat(validator.isValid(now, null)).isFalse();
        // Future : OK
        assertThat(validator.isValid(now.plus(1, ChronoUnit.DAYS), null)).isTrue();
        // Past : KO
        assertThat(validator.isValid(now.minus(1, ChronoUnit.DAYS), null)).isFalse();
        // Null : OK
        assertThat(validator.isValid(null, null)).isTrue();
    }

}
