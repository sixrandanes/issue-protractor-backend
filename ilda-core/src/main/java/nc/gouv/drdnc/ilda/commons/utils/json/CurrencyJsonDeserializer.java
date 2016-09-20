package nc.gouv.drdnc.ilda.commons.utils.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Annotation pour la désérialization des bigDecimal en JSON
 *
 * @author ILDA
 */
public class CurrencyJsonDeserializer extends JsonDeserializer<BigDecimal> {

    @Override
    public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        BigDecimal result = null;
        if (p.getDecimalValue() != null) {
            result = p.getDecimalValue().setScale(5);
        }
        return result;
    }

}
