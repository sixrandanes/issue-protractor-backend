package nc.gouv.dtsi.etudes.commons.utils.json;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;
import java.math.BigDecimal;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Tests unitaires de la classe CurrencyJsonDeserializer
 *
 * @author ILDA
 */
public class CurrencyJsonDeserializerTest {

    private ObjectMapper mapper;

    /**
     * Setup before tests
     *
     * @throws Exception Exception
     */
    @BeforeTest
    public void setUp() throws Exception {
        initMocks(this);
        mapper = new ObjectMapper();

    }

    /**
     * @throws java.lang.Exception
     */
    @AfterTest
    public void tearDown() throws Exception {
        mapper = null;
    }

    /**
     * On est que les 0 soient bien mis à la fin
     * 
     * @throws JsonParseException
     * @throws IOException
     */
    @Test
    public void shouldReturnBigDecimalWithScale() throws JsonParseException, IOException {
        String number = "123.1";
        BigDecimal result = deserialize(number);

        assertThat(result).isEqualTo(new BigDecimal("123.10000"));

    }

    /**
     * 
     * On test que les 5 chiffres soient bien conservés
     * 
     * @throws JsonParseException
     * @throws IOException
     */
    @Test
    public void shouldReturnBigDecimal1() throws JsonParseException, IOException {
        String number = "123.12345";
        BigDecimal result = deserialize(number);

        assertThat(result).isEqualTo(new BigDecimal("123.12345"));

    }

    /**
     * Les 3 chiffres sont bien conservés et ajoutent 2 "0"
     * 
     * @throws JsonParseException
     * @throws IOException
     */
    @Test
    public void shouldReturnBigDecimal2() throws JsonParseException, IOException {
        String number = "123.001";
        BigDecimal result = deserialize(number);

        assertThat(result).isEqualTo(new BigDecimal("123.00100"));

    }

    private BigDecimal deserialize(String number) throws IOException, JsonParseException,
            JsonProcessingException {
        TestObject testObject = mapper.readValue("{\"bigDec\":" + number + "}", TestObject.class);

        return testObject.getBigDec();
    }

    private static class TestObject {

        @JsonDeserialize(using = CurrencyJsonDeserializer.class)
        private final BigDecimal bigDec = null;

        public BigDecimal getBigDec() {
            return bigDec;
        }
    }
}
