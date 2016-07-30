package nc.gouv.dtsi.etudes.commons.utils.json;

import static org.mockito.MockitoAnnotations.initMocks;

import org.testng.annotations.BeforeTest;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Tests unitaires de la classe CurrencyJsonSerializer
 *
 * @author ILDA
 */
public class CurrencyJsonSerializerTest {

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

}
