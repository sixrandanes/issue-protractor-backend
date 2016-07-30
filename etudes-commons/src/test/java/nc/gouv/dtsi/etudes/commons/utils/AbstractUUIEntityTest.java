package nc.gouv.dtsi.etudes.commons.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.testng.annotations.Test;


/**
 * Tests unitaires de la classe Unite
 *
 * @author ILDA
 */
public class AbstractUUIEntityTest {

    /**
     * Test method for {@link Unite#equals(Object)} and {@link Unite#hashCode()}
     */
    @Test
    public void equalsAndHashcode1() {
        // Entités équivalentes
        final Unite ent1 = buildNewInstance();
        final Unite ent2 = buildNewInstance();
        ent1.setId(ent2.getId());

        assertThat(ent1).isEqualTo(ent2);
        assertThat(ent2).isEqualTo(ent1);
        assertThat(ent1.hashCode()).isEqualTo(ent2.hashCode());
    }

    /**
     * Test method for {@link Unite#equals(Object)} and {@link Unite#hashCode()}
     */
    @Test
    public void equalsAndHashcode2() {
        // Id différents
        final Unite ent1 = buildNewInstance();
        final Unite ent2 = buildNewInstance();

        assertThat(ent1).isNotEqualTo(ent2);
        assertThat(ent2).isNotEqualTo(ent1);
        assertThat(ent1.hashCode()).isNotEqualTo(ent2.hashCode());
    }

    /**
     * Test method for {@link Unite#equals(Object)} and {@link Unite#hashCode()}
     */
    @Test
    public void equalsAndHashcode3() {
        // Version num différents (pour tester l'appel au super.equals)
        final Unite ent1 = buildNewInstance();
        final Unite ent2 = buildNewInstance();
        ent1.setId(ent2.getId());

        ent2.setVersionNum(2L);

        assertThat(ent1).isNotEqualTo(ent2);
        assertThat(ent2).isNotEqualTo(ent1);
        assertThat(ent1.hashCode()).isNotEqualTo(ent2.hashCode());
    }

    /**
     * Test method for {@link Unite#equals(Object)}
     */
    @Test
    public void equalsAndHashcode4() {
        final Unite ent1 = buildNewInstance();

        assertThat(ent1).isNotEqualTo(null);
        assertThat(ent1).isNotEqualTo(new Object());
    }

    private Unite buildNewInstance() {
        Unite entt = new Unite();
        entt.setId(UUID.randomUUID());
        return entt;
    }

    private class Unite extends AbstractUUIEntity {

        /** Constructeur par défaut */
        public Unite() {
            super();
        }

        /** serialVersionUID */
        private static final long serialVersionUID = 1L;
    }
}
