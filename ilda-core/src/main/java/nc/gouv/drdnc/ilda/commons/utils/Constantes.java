package nc.gouv.drdnc.ilda.commons.utils;

import java.time.format.DateTimeFormatter;

/**
 * Interface contenant des constantes de l'application
 *
 * @author ILDA
 */
public interface Constantes {

    /**
     * Constantes GMT+11 à utiliser en tant que Locale dans les LocalDate et LocalDateTime
     */
    String GMT11 = "GMT+11";

    /**
     * Constante pour le pattern de date
     */
    String PATTERN_DATE = "dd/MM/yyyy";

    /**
     * Formatteur de dates au format dd/MM/yyyy
     */
    DateTimeFormatter DTF = DateTimeFormatter.ofPattern(PATTERN_DATE);

}
