/**
 *
 */
package nc.gouv.dtsi.etudes.commons.validation.dates;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

import nc.gouv.dtsi.etudes.commons.utils.Constantes;
import nc.gouv.dtsi.etudes.commons.validation.dates.StrictFutureLocalDateValidator;

/**
 * Tests de la classe StrictFutureLocalDateTimeValidator
 *
 * @author FFAIVE
 */
public class StrictFutureLocalDateValidatorTest {

    private StrictFutureLocalDateValidator validator;

    /**
     * Setup before tests
     */
    @Before
    public void setUp() {
        validator = new StrictFutureLocalDateValidator();
    }

    /**
     * Test method for {@link StrictFutureLocalDateValidator#isValid(java.time.LocalDate, javax.validation.ConstraintValidatorContext)} .
     */
    @Test
    public void testIsValid() {

        final LocalDate now = LocalDate.now(ZoneId.of(Constantes.GMT11));

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
