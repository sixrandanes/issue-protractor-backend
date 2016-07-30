package nc.gouv.dtsi.etudes.commons.validation.dates;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

import nc.gouv.dtsi.etudes.commons.utils.Constantes;
import nc.gouv.dtsi.etudes.commons.validation.dates.PastLocalDateTimeValidator;

/**
 * Tests de la classe PastLocalDateTimeValidator
 *
 * @author FFAIVE
 */
public class PastLocalDateTimeValidatorTest {

    private PastLocalDateTimeValidator validator;

    /**
     * Setup before tests
     */
    @Before
    public void setUp() {
        validator = new PastLocalDateTimeValidator();
    }

    /**
     * Test method for {@link PastLocalDateTimeValidator#isValid(java.time.LocalDateTime, javax.validation.ConstraintValidatorContext)} .
     */
    @Test
    public void testIsValid() {

        final LocalDateTime now = LocalDateTime.now(ZoneId.of(Constantes.GMT11));

        // Now : OK
        assertThat(validator.isValid(now.minus(100, ChronoUnit.MILLIS), null)).isTrue();
        // Future : KO
        assertThat(validator.isValid(now.plus(1, ChronoUnit.DAYS), null)).isFalse();
        // Past : OK
        assertThat(validator.isValid(now.minus(1, ChronoUnit.DAYS), null)).isTrue();
        // Null : OK
        assertThat(validator.isValid(null, null)).isTrue();
    }

}
