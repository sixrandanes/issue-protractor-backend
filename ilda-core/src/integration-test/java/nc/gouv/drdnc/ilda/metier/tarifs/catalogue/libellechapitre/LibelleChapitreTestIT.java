package nc.gouv.drdnc.ilda.metier.tarifs.catalogue.libellechapitre;

import static nc.gouv.drdnc.ilda.utils.ConstantesTest.HAL_JSON;
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
import nc.gouv.drdnc.ilda.loader.metier.tarifs.catalogue.libellechapitre.LibelleChapitreLoader;
import nc.gouv.drdnc.ilda.loader.metier.tarifs.catalogue.section.SectionLoader;
import nc.gouv.drdnc.ilda.metier.tarifs.catalogue.chapitre.Chapitre;
import nc.gouv.drdnc.ilda.metier.tarifs.catalogue.chapitre.ChapitreRepository;
import nc.gouv.drdnc.ilda.metier.tarifs.catalogue.section.Section;
import nc.gouv.dtsi.etudes.commons.utils.Constantes;
import nc.gouv.dtsi.etudes.commons.utils.JsonParser;

/**
 * Tests d'intégration de la classe LibelleChapitre
 *
 * @author ILDA
 */
@SpringApplicationConfiguration(classes = IldaApplication.class)
@WebIntegrationTest
public class LibelleChapitreTestIT extends AbstractTestNGSpringContextTests {

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

    @Autowired
    LibelleChapitreLoader libelleChapitreLoader;

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
        libelleChapitreLoader.removeAllData();
        chapitreLoader.removeAllData();
        sectionLoader.removeAllData();
        chapitreLoader.loadData();
    }

    /**
     * Test de la création d'un libellé chapitre. Cas passant
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldCreateLibelleChapitre() throws Exception {
        Section section = sectionLoader.createSection("II", today.plusDays(5), today.plusDays(20));

        Chapitre chapitre = chapitreLoader.createChapitre(new Integer("28"), section, today.plusDays(5), today.plusDays(20), "motif", "motifFinEffet",
                "Libelle shouldCreateLibelleChapitre");

        // Nouvelle devise, sans possibilité de contrainte d'unicité
        final LibelleChapitre entt = new LibelleChapitre();
        entt.setChapitre(chapitre);
        entt.setDesignation("Libelle Defaut");

        entt.setDateEffet(chapitre.getDateEffet().plusDays((1)));
        entt.setMotifEffet("Motif effet");

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/libelles_chapitres")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(HAL_JSON))
                .andExpect(jsonPath("$.designation", equalTo(entt.getDesignation())))
                .andExpect(jsonPath("$.notes", equalTo(entt.getNotes())))
                // Vérifier que les dates et auteur de Création ont été renseignés
                .andExpect(jsonPath("$.auteurCre", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.dateCre", is(notNullValue())));

    }

    /**
     * Test de la création d'un libellé chapitre. Cas passant
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotCreateLibelleChapitre_dateEffetBeforeChapitre() throws Exception {
        Section section = sectionLoader.createSection("II", today.plusDays(5), today.plusDays(20));

        Chapitre chapitre = chapitreLoader.createChapitre(new Integer("28"), section, today.plusDays(8), today.plusDays(20), "motif", "motifFinEffet",
                "Libelle shouldCreateLibelleChapitre");

        // Nouvelle devise, sans possibilité de contrainte d'unicité
        final LibelleChapitre entt = new LibelleChapitre();
        entt.setChapitre(chapitre);
        entt.setDesignation("Libelle Defaut");

        entt.setDateEffet(chapitre.getDateEffet().minusDays((1)));
        entt.setMotifEffet("Motif effet");

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/libelles_chapitres")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());

    }

    /**
     * Test de la création d'un libellé chapitre. Cas passant
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotCreateLibelleChapitre_dateEffetAfterChapitre() throws Exception {
        Section section = sectionLoader.createSection("II", today.plusDays(5), today.plusDays(20));

        Chapitre chapitre = chapitreLoader.createChapitre(new Integer("28"), section, today.plusDays(8), today.plusDays(20), "motif", "motifFinEffet",
                "Libelle shouldCreateLibelleChapitre");

        // Nouvelle devise, sans possibilité de contrainte d'unicité
        final LibelleChapitre entt = new LibelleChapitre();
        entt.setChapitre(chapitre);
        entt.setDesignation("Libelle Defaut");

        entt.setDateEffet(chapitre.getDateFinEffet().plusDays((1)));
        entt.setMotifEffet("Motif effet");

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/libelles_chapitres")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());

    }

    /**
     * Test de la création d'un libellé chapitre. Cas non passant
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotCreateLibelleChapitre_missingMotif() throws Exception {
        Section section = sectionLoader.createSection("II", today.plusDays(5), today.plusDays(20));

        Chapitre chapitre = chapitreLoader.createChapitre(new Integer("28"), section, today.plusDays(8), today.plusDays(20), "motif", "motifFinEffet",
                "Libelle shouldCreateLibelleChapitre");

        // Nouvelle devise, sans possibilité de contrainte d'unicité
        final LibelleChapitre entt = new LibelleChapitre();
        entt.setChapitre(chapitre);
        entt.setDesignation("Libelle Defaut");

        entt.setDateEffet(chapitre.getDateEffet().plusDays((1)));

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/libelles_chapitres")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());

    }

    /**
     * Test de la création d'un libellé chapitre. Cas non passant
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotCreateLibelleChapitre_libelleWithNullDateEffetExists() throws Exception {
        Section section = sectionLoader.createSection("II", today.plusDays(5), today.plusDays(20));

        Chapitre chapitre = chapitreLoader.createChapitre(new Integer("28"), section, today.plusDays(8), today.plusDays(20), "motif", "motifFinEffet",
                "Libelle shouldCreateLibelleChapitre");

        // Nouvelle devise, sans possibilité de contrainte d'unicité
        final LibelleChapitre entt = new LibelleChapitre();
        entt.setChapitre(chapitre);
        entt.setDesignation("Libelle Defaut");

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/libelles_chapitres")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(HAL_JSON))
                .andExpect(jsonPath("$.designation", equalTo(entt.getDesignation())))
                .andExpect(jsonPath("$.notes", equalTo(entt.getNotes())))
                // Vérifier que les dates et auteur de Création ont été renseignés
                .andExpect(jsonPath("$.auteurCre", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.dateCre", is(notNullValue())));

        mockMvc.perform(post("http://localhost/api/libelles_chapitres")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());

    }

    /**
     * Test de la création d'un libellé chapitre. Cas non passant
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotCreateLibelleChapitre_designationExists() throws Exception {
        Section section = sectionLoader.createSection("II", today.plusDays(5), today.plusDays(20));

        Chapitre chapitre = chapitreLoader.createChapitre(new Integer("28"), section, today.plusDays(8), today.plusDays(20), "motif", "motifFinEffet",
                "Libelle shouldCreateLibelleChapitre");

        // Nouvelle devise, sans possibilité de contrainte d'unicité
        final LibelleChapitre entt = new LibelleChapitre();
        entt.setChapitre(chapitre);
        entt.setDesignation("Libelle Defaut");
        entt.setDateEffet(entt.getChapitre().getDateEffet().plusDays(2));
        entt.setMotifEffet("motif");

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/libelles_chapitres")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(HAL_JSON))
                .andExpect(jsonPath("$.designation", equalTo(entt.getDesignation())))
                .andExpect(jsonPath("$.notes", equalTo(entt.getNotes())))
                // Vérifier que les dates et auteur de Création ont été renseignés
                .andExpect(jsonPath("$.auteurCre", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.dateCre", is(notNullValue())));

        entt.setDateEffet(entt.getDateEffet().plusDays(1));
        jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/libelles_chapitres")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());

    }

    /**
     * Test de la création d'un libellé chapitre. Cas non passant
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotCreateLibelleChapitre_libelleChapitreExistsAtDateEffet() throws Exception {
        Section section = sectionLoader.createSection("II", today.plusDays(5), today.plusDays(20));

        Chapitre chapitre = chapitreLoader.createChapitre(new Integer("28"), section, today.plusDays(8), today.plusDays(20), "motif", "motifFinEffet",
                "Libelle shouldCreateLibelleChapitre");

        // Nouvelle devise, sans possibilité de contrainte d'unicité
        final LibelleChapitre entt = new LibelleChapitre();
        entt.setChapitre(chapitre);
        entt.setDesignation("Libelle Defaut");
        entt.setDateEffet(entt.getChapitre().getDateEffet().plusDays(2));
        entt.setMotifEffet("motif");

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/libelles_chapitres")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(HAL_JSON))
                .andExpect(jsonPath("$.designation", equalTo(entt.getDesignation())))
                .andExpect(jsonPath("$.notes", equalTo(entt.getNotes())))
                // Vérifier que les dates et auteur de Création ont été renseignés
                .andExpect(jsonPath("$.auteurCre", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.dateCre", is(notNullValue())));

        entt.setDesignation("Nouvelle designation mais fail parce qu'il y a déjà un libellé à cette date");
        jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/libelles_chapitres")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());

    }

    /**
     * Test de la création d'un libellé chapitre. Cas passant
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotCreateLibelleChapitre_TwoDifferentLibelleButTwoFutur() throws Exception {
        Section section = sectionLoader.createSection("II", today.plusDays(5), today.plusDays(20));

        Chapitre chapitre = chapitreLoader.createChapitre(new Integer("28"), section, today.plusDays(8), today.plusDays(20), "motif", "motifFinEffet",
                "Libelle shouldCreateLibelleChapitre");

        // Nouvelle devise, sans possibilité de contrainte d'unicité
        final LibelleChapitre entt = new LibelleChapitre();
        entt.setChapitre(chapitre);
        entt.setDesignation("Libelle Defaut");
        entt.setDateEffet(entt.getChapitre().getDateEffet().plusDays(2));
        entt.setMotifEffet("motif");

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/libelles_chapitres")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(HAL_JSON))
                .andExpect(jsonPath("$.designation", equalTo(entt.getDesignation())))
                .andExpect(jsonPath("$.notes", equalTo(entt.getNotes())))
                // Vérifier que les dates et auteur de Création ont été renseignés
                .andExpect(jsonPath("$.auteurCre", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.dateCre", is(notNullValue())));

        entt.setDesignation("Nouvelle designation success");
        entt.setDateEffet(entt.getDateEffet().plusDays(1));
        jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/libelles_chapitres")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());
    }

    /**
     * Préparation du JSON au format HAL+JSON
     *
     * @param ent une entité à transformer
     * @return JSON de l'entité, au format HAL+JSON
     * @throws Exception en cas d'erreur de parsing
     */
    private static String prepareJSON(final LibelleChapitre ent) throws Exception {

        String json = JsonParser.asJsonString(ent);
        json = json.replaceFirst("\"chapitre\":\\{\\}", "\"chapitre\":\"http://localhost/api/chapitres/" + ent.getChapitreId() +
                "\"");
        return json;
    }
}
