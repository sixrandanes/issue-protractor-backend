package nc.gouv.drdnc.ilda.metier.tarifs.catalogue.chapitre;

import static nc.gouv.drdnc.ilda.utils.ConstantesTest.HAL_JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import nc.gouv.drdnc.ilda.IldaApplication;
import nc.gouv.drdnc.ilda.loader.metier.tarifs.catalogue.chapitre.ChapitreLoader;
import nc.gouv.drdnc.ilda.loader.metier.tarifs.catalogue.section.SectionLoader;
import nc.gouv.drdnc.ilda.metier.tarifs.catalogue.libellechapitre.LibelleChapitre;
import nc.gouv.drdnc.ilda.metier.tarifs.catalogue.libellechapitre.LibelleChapitreRepository;
import nc.gouv.drdnc.ilda.metier.tarifs.catalogue.section.Section;
import nc.gouv.dtsi.etudes.commons.utils.Constantes;
import nc.gouv.dtsi.etudes.commons.utils.JsonParser;

/**
 * Tests d'intégration de la classe Chapitre
 *
 * @author ILDA
 */
@SpringApplicationConfiguration(classes = IldaApplication.class)
@WebIntegrationTest
public class ChapitreTestIT extends AbstractTestNGSpringContextTests {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    ChapitreRepository chapitreRepo;

    @Autowired
    LibelleChapitreRepository libelleChapitreRepository;

    @Autowired
    ChapitreLoader chapitreLoader;

    @Autowired
    SectionLoader sectionLoader;

    private LocalDate today = LocalDate.now(ZoneId.of(Constantes.GMT11));

    /**
     * Setup before tests
     *
     * @throws Exception Exception
     */
    @BeforeTest
    public void setUp() throws Exception {
        this.springTestContextPrepareTestInstance();
        mockMvc = MockMvcBuilders.webAppContextSetup(context).dispatchOptions(true).build();
    }

    /**
     * Suppression de la base à chaque test pour être sur que l'on part sur un jeu de données clean
     *
     * @throws Exception Exception
     */
    @BeforeMethod
    public void before() throws Exception {
        chapitreLoader.removeAllData();
        sectionLoader.removeAllData();
        chapitreLoader.loadData();
    }

    /**
     * Test de la création d'un Chapitre. Cas passant
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldCreateChapitreWithDates() throws Exception {

        Section section = sectionLoader.createSection("II", today.plusDays(5), this.today.plusDays(20));
        // Nouvelle devise, sans possibilité de contrainte d'unicité
        final Chapitre entt = new Chapitre();
        entt.setNumeroChapitre(new Integer("29"));
        entt.setSection(section);
        entt.setDateEffet(section.getDateEffet());
        entt.setMotifEffet("Motif");
        entt.setDateFinEffet(section.getDateFinEffet());
        entt.setMotifFinEffet("motif de fin d'effet");
        entt.setDesignationLibelleDefaut("Libelle Defaut");

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/chapitres")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(HAL_JSON))
                .andExpect(jsonPath("$.numeroChapitre", equalTo(entt.getNumeroChapitre())))
                // Vérifier que les dates et auteur de Création ont été renseignés
                .andExpect(jsonPath("$.auteurCre", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.dateCre", is(notNullValue())));

        List<LibelleChapitre> libelleChapitre = libelleChapitreRepository.findByDesignation("Libelle Defaut");
        assertThat(libelleChapitre).hasSize(1);
    }

    /**
     * Test de la création d'un chapitre. Cas passant
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldCreateChapitre_NoDates() throws Exception {

        Section section = sectionLoader.createSection("II", today.plusDays(5), this.today.plusDays(20));
        // Nouvelle devise, sans possibilité de contrainte d'unicité
        final Chapitre entt = new Chapitre();
        entt.setNumeroChapitre(new Integer("29"));
        entt.setSection(section);
        entt.setDesignationLibelleDefaut("Libelle Defaut");

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/chapitres")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(HAL_JSON))
                .andExpect(jsonPath("$.numeroChapitre", equalTo(entt.getNumeroChapitre())))
                // Vérifier que les dates et auteur de Création ont été renseignés
                .andExpect(jsonPath("$.auteurCre", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.dateCre", is(notNullValue())));

        List<LibelleChapitre> libelleChapitre = libelleChapitreRepository.findByDesignation("Libelle Defaut");
        assertThat(libelleChapitre).hasSize(1);
    }

    /**
     * Test de la création d'un chapitre. Cas non passant
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotCreateChapitre_dateEffetBeforeSection() throws Exception {

        Section section = sectionLoader.createSection("II", today.plusDays(5), this.today.plusDays(20));
        // Nouvelle devise, sans possibilité de contrainte d'unicité
        final Chapitre entt = new Chapitre();
        entt.setNumeroChapitre(new Integer("29"));
        entt.setSection(section);
        entt.setDesignationLibelleDefaut("Libelle Defaut");

        entt.setDateEffet(section.getDateEffet().minusDays(1));
        entt.setMotifEffet("motif");

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/chapitres")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());

        List<LibelleChapitre> libelleChapitre = libelleChapitreRepository.findByDesignation("Libelle Defaut");
        assertThat(libelleChapitre).hasSize(0);
    }

    /**
     * Test de la création d'un chapitre. Cas non passant
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotCreateChapitre_dateEffetAfterSection() throws Exception {

        Section section = sectionLoader.createSection("II", today.plusDays(5), this.today.plusDays(20));
        // Nouvelle devise, sans possibilité de contrainte d'unicité
        final Chapitre entt = new Chapitre();
        entt.setNumeroChapitre(new Integer("29"));
        entt.setSection(section);
        entt.setDesignationLibelleDefaut("Libelle Defaut");

        entt.setDateEffet(section.getDateFinEffet().plusDays(1));
        entt.setMotifEffet("motif");

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/chapitres")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());

        List<LibelleChapitre> libelleChapitre = libelleChapitreRepository.findByDesignation("Libelle Defaut");
        assertThat(libelleChapitre).hasSize(0);
    }

    /**
     * Test de la création d'un chapitre. Cas non passant
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotCreateChapitre_ChapitreBefore() throws Exception {

        Section section = sectionLoader.createSection("II", today.plusDays(5), this.today.plusDays(20));
        // Nouvelle devise, sans possibilité de contrainte d'unicité
        final Chapitre entt = new Chapitre();
        entt.setNumeroChapitre(new Integer("29"));
        entt.setSection(section);
        entt.setDesignationLibelleDefaut("Libelle Defaut");

        entt.setDateEffet(section.getDateEffet().minusDays(3));
        entt.setMotifEffet("motif");
        entt.setDateFinEffet(section.getDateEffet().minusDays(2));
        entt.setMotifFinEffet("motif");

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/chapitres")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());

        List<LibelleChapitre> libelleChapitre = libelleChapitreRepository.findByDesignation("Libelle Defaut");
        assertThat(libelleChapitre).hasSize(0);
    }

    /**
     * Test de la création d'un chapitre. Cas non passant
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotCreateChapitre_ChapitreBeforeChevauche() throws Exception {

        Section section = sectionLoader.createSection("II", today.plusDays(5), this.today.plusDays(20));
        // Nouvelle devise, sans possibilité de contrainte d'unicité
        final Chapitre entt = new Chapitre();
        entt.setNumeroChapitre(new Integer("29"));
        entt.setSection(section);
        entt.setDesignationLibelleDefaut("Libelle Defaut");

        entt.setDateEffet(section.getDateEffet().minusDays(3));
        entt.setMotifEffet("motif");
        entt.setDateFinEffet(section.getDateEffet().plusDays(2));
        entt.setMotifFinEffet("motif");

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/chapitres")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());

        List<LibelleChapitre> libelleChapitre = libelleChapitreRepository.findByDesignation("Libelle Defaut");
        assertThat(libelleChapitre).hasSize(0);
    }

    /**
     * Test de la création d'un chapitre. Cas non passant
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotCreateChapitre_ChapitreAfterChevauche() throws Exception {

        Section section = sectionLoader.createSection("II", today.plusDays(5), this.today.plusDays(20));
        // Nouvelle devise, sans possibilité de contrainte d'unicité
        final Chapitre entt = new Chapitre();
        entt.setNumeroChapitre(new Integer("29"));
        entt.setSection(section);
        entt.setDesignationLibelleDefaut("Libelle Defaut");

        entt.setDateEffet(section.getDateEffet().plusDays(3));
        entt.setMotifEffet("motif");
        entt.setDateFinEffet(section.getDateFinEffet().plusDays(2));
        entt.setMotifFinEffet("motif");

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/chapitres")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());
        List<LibelleChapitre> libelleChapitre = libelleChapitreRepository.findByDesignation("Libelle Defaut");
        assertThat(libelleChapitre).hasSize(0);
    }

    /**
     * Test de la création d'un chapitre. Cas non passant
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotCreateChapitre_ChapitreAfterSection() throws Exception {

        Section section = sectionLoader.createSection("II", today.plusDays(5), this.today.plusDays(20));
        // Nouvelle devise, sans possibilité de contrainte d'unicité
        final Chapitre entt = new Chapitre();
        entt.setNumeroChapitre(new Integer("29"));
        entt.setSection(section);
        entt.setDesignationLibelleDefaut("Libelle Defaut");

        entt.setDateEffet(section.getDateFinEffet().plusDays(1));
        entt.setMotifEffet("motif");
        entt.setDateFinEffet(section.getDateFinEffet().plusDays(2));
        entt.setMotifFinEffet("motif");

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/chapitres")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());

        List<LibelleChapitre> libelleChapitre = libelleChapitreRepository.findByDesignation("Libelle Defaut");
        assertThat(libelleChapitre).hasSize(0);

    }

    /**
     * Test de la création d'un chapitre. Cas non passant
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotCreateChapitre_ChapitreWithDateEffetNoDesignation() throws Exception {

        Section section = sectionLoader.createSection("II", today.plusDays(5), this.today.plusDays(20));
        // Nouvelle devise, sans possibilité de contrainte d'unicité
        final Chapitre entt = new Chapitre();
        entt.setNumeroChapitre(new Integer("29"));
        entt.setSection(section);

        entt.setDateEffet(section.getDateEffet().plusDays(1));
        entt.setMotifEffet("motif");

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/chapitres")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());

        List<LibelleChapitre> libelleChapitre = libelleChapitreRepository.findByDesignation("Libelle Defaut");
        assertThat(libelleChapitre).hasSize(0);

    }

    /**
     * Préparation du JSON au format HAL+JSON
     *
     * @param ent une entité à transformer
     * @return JSON de l'entité, au format HAL+JSON
     * @throws Exception en cas d'erreur de parsing
     */
    private static String prepareJSON(final Chapitre ent) throws Exception {

        String json = JsonParser.asJsonString(ent);
        if (ent.getLibellesChapitres() != null && !ent.getLibellesChapitres().isEmpty()) {
            String jsonTx = "";
            int cpt = 0;
            for (LibelleChapitre tx : ent.getLibellesChapitres()) {
                if (cpt == 0) {
                    cpt++;
                    jsonTx += JsonParser.asJsonString(tx);

                } else {
                    jsonTx += "," + JsonParser.asJsonString(tx);
                }
            }
            json = json.replaceFirst("\"libelleChapitre\":\\[\\{\\},\\{\\}\\]",
                    "\"libelleChapitre\":\\[" + jsonTx + "\\]");
        }
        json = json.replaceFirst("\"section\":\\{\\}", "\"section\":\"http://localhost/api/sections/" + ent.getSectionId() +
                "\"");
        return json;
    }
}
