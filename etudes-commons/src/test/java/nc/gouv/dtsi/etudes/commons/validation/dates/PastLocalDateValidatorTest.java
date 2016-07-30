package nc.gouv.dtsi.etudes.commons.validation.dates;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

import nc.gouv.dtsi.etudes.commons.utils.Constantes;
import nc.gouv.dtsi.etudes.commons.validation.dates.PastLocalDateValidator;

/**
 * Tests de la classe PastLocalDateValidator
 *
 * @author FFAIVE
 */
public class PastLocalDateValidatorTest {

    private PastLocalDateValidator validator;

    /**
     * Setup before tests
     */
    @Before
    public void setUp() {
        validator = new PastLocalDateValidator();
    }

    /**
     * Test method for {@link PastLocalDateValidator#isValid(java.time.LocalDate, javax.validation.ConstraintValidatorContext)} .
     */
    @Test
    public void testIsValid() {

        final LocalDate now = LocalDate.now(ZoneId.of(Constantes.GMT11));

        // Now : OK
        assertThat(validator.isValid(now, null)).isTrue();
        // Future : KO
        assertThat(validator.isValid(now.plus(1, ChronoUnit.DAYS), null)).isFalse();
        // Past : OK
        assertThat(validator.isValid(now.minus(1, ChronoUnit.DAYS), null)).isTrue();
        // Null : OK
        assertThat(validator.isValid(null, null)).isTrue();
    }

}
