package nc.gouv.dtsi.etudes.commons.utils.json;

import java.io.IOException;
import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Annotation pour la sérialization des bigDecimal en JSON
 *
 * @author ILDA
 */
public class CurrencyJsonSerializer extends JsonSerializer<BigDecimal> {

    /**
     * Méthode qui permet de sérializer en JSON un big décimal au bon format {@inheritDoc}
     */
    @Override
    public void serialize(BigDecimal value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        if (value != null) {
            jgen.writeString(value.setScale(5).stripTrailingZeros().toString());
        }
    }
}
