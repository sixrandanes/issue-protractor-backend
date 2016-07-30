package nc.gouv.dtsi.etudes.commons.validation.dates;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

import nc.gouv.dtsi.etudes.commons.utils.Constantes;
import nc.gouv.dtsi.etudes.commons.validation.dates.StrictPastLocalDateTimeValidator;

/**
 * Tests de la classe StrictPastLocalDateTimeValidator
 *
 * @author FFAIVE
 */
public class StrictPastLocalDateTimeValidatorTest {

    private StrictPastLocalDateTimeValidator validator;

    /**
     * Setup before tests
     */
    @Before
    public void setUp() {
        validator = new StrictPastLocalDateTimeValidator();
    }

    /**
     * Test method for {@link StrictPastLocalDateTimeValidator#isValid(java.time.LocalDateTime, javax.validation.ConstraintValidatorContext)} .
     */
    @Test
    public void testIsValid() {

        // Ajouter 15 ms au "now" pour rendre le test moins aléatoire
        final LocalDateTime now = LocalDateTime.now(ZoneId.of(Constantes.GMT11)).plus(15, ChronoUnit.MILLIS);
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
