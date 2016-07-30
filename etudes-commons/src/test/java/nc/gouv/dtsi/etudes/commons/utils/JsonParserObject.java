package nc.gouv.dtsi.etudes.commons.utils;

import java.math.BigDecimal;

/**
 * Objet utilisé pour le test du parser JSON
 * 
 * @author FFAIE
 *
 */
public class JsonParserObject {

    private String attributeString;
    private Integer attributeInt;
    private Long attributeLong;
    private BigDecimal attributBigDecimal;

    /**
     * @return the attributeString
     */
    public String getAttributeString() {
        return attributeString;
    }

    /**
     * @param attributeString the attributeString to set
     */
    public void setAttributeString(final String attributeString) {
        this.attributeString = attributeString;
    }

    /**
     * @return the attributeInt
     */
    public Integer getAttributeInt() {
        return attributeInt;
    }

    /**
     * @param attributeInt the attributeInt to set
     */
    public void setAttributeInt(final Integer attributeInt) {
        this.attributeInt = attributeInt;
    }

    /**
     * @return the attributeLong
     */
    public Long getAttributeLong() {
        return attributeLong;
    }

    /**
     * @param attributeLong the attributeLong to set
     */
    public void setAttributeLong(final Long attributeLong) {
        this.attributeLong = attributeLong;
    }

    /**
     * @return the attributBigDecimal
     */
    public BigDecimal getAttributBigDecimal() {
        return attributBigDecimal;
    }

    /**
     * @param attributBigDecimal the attributBigDecimal to set
     */
    public void setAttributBigDecimal(final BigDecimal attributBigDecimal) {
        this.attributBigDecimal = attributBigDecimal;
    }

}
