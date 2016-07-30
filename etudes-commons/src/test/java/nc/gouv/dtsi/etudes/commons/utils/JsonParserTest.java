package nc.gouv.dtsi.etudes.commons.utils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

import nc.gouv.dtsi.etudes.commons.utils.AbstractEntity;

@RunWith(MockitoJUnitRunner.class)

public class JsonParserTest {

    private static final String STRING_TEST = "JSON PARSE ME";

    @Test
    public void shouldReturnJson() throws Exception {
        final JsonParserObject objectTest = new JsonParserObject();
        objectTest.setAttributBigDecimal(new BigDecimal(1));
        objectTest.setAttributeInt(2);
        objectTest.setAttributeLong(new Long(4));
        objectTest.setAttributeString(STRING_TEST);

        final String result = JsonParser.asJsonString(objectTest);

        assertThat(result).contains("\"attributeString\":\"" + objectTest.getAttributeString());
        assertThat(result).contains("\"attributeInt\":" + String.valueOf(objectTest.getAttributeInt()));
        assertThat(result).contains("\"attributeLong\":" + String.valueOf(objectTest.getAttributeLong()));
        assertThat(result).contains("\"attributBigDecimal\":" + String.valueOf(objectTest.getAttributBigDecimal()));
    }

    @Test
    public void shouldReturnJsonCertif() throws Exception {

        final Certif ent = new Certif();
        ent.setAuteurCre("DTSI_CRE");
        ent.setDateCre(LocalDateTime.of(1983, 3, 9, 23, 30, 25));
        ent.setId(5L);
        ent.setIntitule("Intitule test");
        ent.setFichier(new Fichier());

        String result;
        result = JsonParser.asJsonString(ent);

        assertThat(result).contains("\"auteurCre\":\"DTSI_CRE\"");
        assertThat(result).contains("\"dateCre\":\"09/03/1983 23:30:25\"");
        assertThat(result).contains("\"id\":5");
        assertThat(result).contains("\"intitule\":\"Intitule test\"");
        assertThat(result).contains("\"fichier\":{}");
        assertThat(result).contains("\"codesRome\":[{},{}]");
        assertThat(result).contains("\"cpus\":[{},{}]");
    }

    private class Fichier extends AbstractEntity {

        /** */
        private static final long serialVersionUID = 436997350669780006L;

        public Fichier() {
            super();
        }

        @Override
        public Long getId() {
            return 5L;
        }
    }

    @SuppressWarnings("rawtypes")
    private class Certif extends AbstractEntity {

        /** */
        private static final long serialVersionUID = -821490718044598220L;
        private Fichier fichier = new Fichier();
        private List codesRome = new ArrayList();
        private Set cpus = new HashSet();
        private String intitule;
        private Long id;

        public Certif() {
            super();
        }

        @Override
        public Long getId() {
            return id;
        }

        /**
         * Retourne la valeur du champ fichier
         *
         * @return {@link #fichier}
         */
        public Fichier getFichier() {
            return fichier;
        }

        /**
         * Renseigne la valeur du champ fichier
         *
         * @param aFichier valeur à setter pour {@link #fichier}
         */
        public void setFichier(final Fichier aFichier) {
            fichier = aFichier;
        }

        /**
         * Retourne la valeur du champ codesRome
         *
         * @return {@link #codesRome}
         */
        public List getCodesRome() {
            return codesRome;
        }

        /**
         * Renseigne la valeur du champ codesRome
         *
         * @param aCodesRome valeur à setter pour {@link #codesRome}
         */
        public void setCodesRome(final List aCodesRome) {
            codesRome = aCodesRome;
        }

        /**
         * Retourne la valeur du champ cpus
         *
         * @return {@link #cpus}
         */
        public Set getCpus() {
            return cpus;
        }

        /**
         * Renseigne la valeur du champ cpus
         *
         * @param aCpus valeur à setter pour {@link #cpus}
         */
        public void setCpus(final Set aCpus) {
            cpus = aCpus;
        }

        /**
         * Retourne la valeur du champ intitule
         *
         * @return {@link #intitule}
         */
        public String getIntitule() {
            return intitule;
        }

        /**
         * Renseigne la valeur du champ intitule
         *
         * @param aIntitule valeur à setter pour {@link #intitule}
         */
        public void setIntitule(final String aIntitule) {
            intitule = aIntitule;
        }

        /**
         * Renseigne la valeur du champ id
         *
         * @param aId valeur à setter pour {@link #id}
         */
        public void setId(final Long aId) {
            id = aId;
        }

    }
}
