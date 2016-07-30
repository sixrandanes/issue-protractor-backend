package nc.gouv.dtsi.etudes.commons.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

import nc.gouv.dtsi.etudes.commons.utils.BooleanToNumberConverter;

@RunWith(MockitoJUnitRunner.class)
public class BooleantoNumberConverterTest {

    @InjectMocks
    BooleanToNumberConverter converter = new BooleanToNumberConverter();

    @Test
    public void testConvertToDatabaseColumn() {
        Long result = converter.convertToDatabaseColumn(Boolean.TRUE);
        assertThat(result).isEqualTo(Long.valueOf(1));

        result = converter.convertToDatabaseColumn(Boolean.FALSE);
        assertThat(result).isEqualTo(Long.valueOf(0));
        result = converter.convertToDatabaseColumn(null);
        assertThat(result).isEqualTo(Long.valueOf(0));
    }

    @Test
    public void testConverToEntityAttribute() {
        Boolean result = converter.convertToEntityAttribute(Long.valueOf(0));
        assertThat(result).isFalse();

        result = converter.convertToEntityAttribute(Long.valueOf(1));
        assertThat(result).isTrue();

        result = converter.convertToEntityAttribute(null);
        assertThat(result).isFalse();

    }
}
