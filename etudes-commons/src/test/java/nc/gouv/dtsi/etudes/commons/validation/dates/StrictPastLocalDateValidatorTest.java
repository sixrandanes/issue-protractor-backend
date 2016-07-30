package nc.gouv.dtsi.etudes.commons.validation.dates;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

import nc.gouv.dtsi.etudes.commons.utils.Constantes;
import nc.gouv.dtsi.etudes.commons.validation.dates.StrictPastLocalDateValidator;

/**
 * Tests de la classe StrictPastLocalDateValidator
 *
 * @author FFAIVE
 */
public class StrictPastLocalDateValidatorTest {

    private StrictPastLocalDateValidator validator;

    /**
     * Setup before tests
     */
    @Before
    public void setUp() {
        validator = new StrictPastLocalDateValidator();
    }

    /**
     * Test method for {@link StrictPastLocalDateValidator#isValid(java.time.LocalDate, javax.validation.ConstraintValidatorContext)} .
     */
    @Test
    public void testIsValid() {

        final LocalDate now = LocalDate.now(ZoneId.of(Constantes.GMT11));

        // Now : KO
        assertThat(validator.isValid(now, null)).isFalse();
        // Future : KO
        assertThat(validator.isValid(now.plus(1, ChronoUnit.DAYS), null)).isFalse();
        // Past : OK
        assertThat(validator.isValid(now.minus(1, ChronoUnit.DAYS), null)).isTrue();
        // Null : OK
        assertThat(validator.isValid(null, null)).isTrue();
    }

}
