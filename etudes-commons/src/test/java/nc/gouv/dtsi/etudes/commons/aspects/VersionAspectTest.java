/**
 *
 */
package nc.gouv.dtsi.etudes.commons.aspects;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import nc.gouv.dtsi.etudes.commons.utils.AbstractEntity;
import nc.gouv.dtsi.etudes.commons.utils.IEntity;

/**
 * Test unitaire de l'aspect gérant les champs historique
 *
 * @author ILDA
 */
public class VersionAspectTest {

    /**
     *
     */
    @Test
    public void shouldUpdateCreationData() {
        VersionAspectClass aspect = new VersionAspectClass();

        // Given
        final Piece ent = new Piece();

        // When
        aspect.onSaveEnt(ent);

        // Verify that
        assertThat(ent.getAuteurCre()).isNotNull();
        assertThat(ent.getDateCre()).isNotNull();
        assertThat(ent.getAuteurMaj()).isNull();
        assertThat(ent.getDateMaj()).isNull();
    }

    /**
     *
     */
    @Test
    public void shouldUpdateMajData() {
        VersionAspectClass aspect = new VersionAspectClass();

        // Given
        final Piece ent = new Piece();
        ent.setId(1L);

        // When
        aspect.onSaveEnt(ent);

        // Verify that
        assertThat(ent.getAuteurCre()).isNull();
        assertThat(ent.getDateCre()).isNull();
        assertThat(ent.getAuteurMaj()).isNotNull();
        assertThat(ent.getDateMaj()).isNotNull();
    }

    /**
     *
     */
    @Test
    public void shouldAcceptNulls() {
        VersionAspectClass aspect = new VersionAspectClass();

        // Given
        final Piece ent = null;

        // When
        aspect.onSaveEnt(ent);

        // Verify that
        assertThat(ent).isNull();
    }

    /**
     * Test method for {@link VersionAspect#onSaveListEnt(List)}.
     */
    @Test
    public void shouldUpdateCreationList() {
        final List<IEntity> lPiece = new ArrayList<>();
        VersionAspectClass aspect = new VersionAspectClass();

        // Given
        final Piece ent1 = new Piece();
        final Piece ent2 = new Piece();
        lPiece.add(ent1);
        lPiece.add(ent2);

        // When
        aspect.onSaveListEnt(lPiece);

        // Verify that
        assertThat(ent1.getAuteurCre()).isNotNull();
        assertThat(ent1.getDateCre()).isNotNull();
        assertThat(ent1.getAuteurMaj()).isNull();
        assertThat(ent1.getDateMaj()).isNull();

        // Verify that
        assertThat(ent2.getAuteurCre()).isNotNull();
        assertThat(ent2.getDateCre()).isNotNull();
        assertThat(ent2.getAuteurMaj()).isNull();
        assertThat(ent2.getDateMaj()).isNull();
    }

    /**
     * Test permettant juste de verifier qu'aucune exception n'est levee lorsque la liste ne comprends pas des objets de type IEntity
     */
    @Test
    public void shouldNotRaiseException() {
        final List<Object> permissions = Arrays.asList(new Object(), new Object(), new Object());
        VersionAspectClass aspect = new VersionAspectClass();

        aspect.onSaveListEnt(permissions);
    }

    private final class Piece extends AbstractEntity {

        /**
         * serialVersionUID
         */
        private static final long serialVersionUID = 1L;

        private Long id;

        public Piece() {
            super();
        }

        @Override
        public Long getId() {
            return id;
        }

        public void setId(final Long aId) {
            id = aId;
        }
    }

    private class VersionAspectClass extends VersionAspect {

        public VersionAspectClass() {
            super();
        }

        @Override
        protected String getCurrentUserName() {
            return "Test";
        }
    }

}
