package nc.gouv.drdnc.ilda.metier.referentiel.devise;

import static nc.gouv.drdnc.ilda.utils.ConstantesTest.HAL_JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import nc.gouv.drdnc.ilda.loader.metier.referentiel.devise.DeviseLoader;
import nc.gouv.drdnc.ilda.metier.referentiel.tauxchange.TauxChange;
import nc.gouv.dtsi.etudes.commons.utils.Constantes;
import nc.gouv.dtsi.etudes.commons.utils.JsonParser;

/**
 * Tests d'intégration de la classe Devise
 *
 * @author ILDA
 */
@SpringApplicationConfiguration(classes = IldaApplication.class)
@WebIntegrationTest
public class DeviseTestIT extends AbstractTestNGSpringContextTests {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private DeviseRepository deviseRepo;

    @Autowired
    private DeviseLoader deviseLoader;

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
        deviseLoader.removeAllData();
        deviseLoader.loadData();
    }

    /**
     * Test de la recherche des Desvises valides
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldReturnAllValidUnites() throws Exception {

        int size = deviseRepo.findValid().size();

        mockMvc.perform(get("http://localhost/api/devises/search/valid"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(HAL_JSON))
                .andExpect(jsonPath("$._embedded.devises", hasSize(size)));
    }

    /**
     * Test de la création d'une Devise. Cas passant
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldCreateDevise_NoDates() throws Exception {

        // Nouvelle devise, sans possibilité de contrainte d'unicité
        final Devise entt = new Devise();
        entt.setCode("TST");
        entt.setDesignation("Devise de test");
        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/devises")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(HAL_JSON))
                .andExpect(jsonPath("$.code", equalTo(entt.getCode())))
                .andExpect(jsonPath("$.designation", equalTo(entt.getDesignation())))
                // Vérifier que les dates et auteur de Création ont été renseignés
                .andExpect(jsonPath("$.auteurCre", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.dateCre", is(notNullValue())));
    }

    /**
     * Test de la création d'une Devise. Cas passant avec la création d'une devise plus tôt
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldCreateDevise_WithDatesBefore() throws Exception {
        deviseLoader.createDevise("TT1", "Devise de test 1", today.minusYears(2).plusDays(1), today.minusYears(1));
        deviseLoader.createDevise("TT1", "Devise de test 1", today.minusYears(1).plusDays(1), today.minusYears(0));

        Devise devise = deviseRepo.findByCodeAndDateEffet("TTT", today.plusDays(1));
        assertThat(devise).isNull();

        final Devise entt = new Devise();
        entt.setCode("TT1");
        entt.setDesignation("Devise de test 1");
        entt.setDateEffet(today.plusDays(1));
        entt.setDateFinEffet(today.plusYears(2));

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/devises")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(HAL_JSON))
                .andExpect(jsonPath("$.code", equalTo(entt.getCode())))
                .andExpect(jsonPath("$.designation", equalTo(entt.getDesignation())))
                // Vérifier que les dates et auteur de Création ont été renseignés
                .andExpect(jsonPath("$.auteurCre", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.dateCre", is(notNullValue())));

        devise = deviseRepo.findByCodeAndDateEffet("TT1", today.plusDays(1));
        assertThat(devise).isNotNull();
    }

    /**
     * Test de la création d'une Devise. Cas passant avec la création d'une devise après deux devises
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldCreateDevise_WithDatesAfter() throws Exception {
        deviseLoader.createDevise("TT2", "Devise de test 2", today.minusYears(3), today.minusYears(2));
        deviseLoader.createDevise("TT2", "Devise de test 2", today.minusYears(1), today);

        final Devise entt = new Devise();
        entt.setCode("TT2");
        entt.setDesignation("Devise de test 2");
        entt.setDateEffet(today.plusDays(9));
        entt.setDateFinEffet(today.plusYears(10));


        String jsonCreate = prepareJSON(entt);

        // On vérifier que la devise n'est pas présente dans la base;
        Devise devise = deviseRepo.findByCodeAndDateEffet("TTT", today.plusDays(9));
        assertThat(devise).isNull();

        mockMvc.perform(post("http://localhost/api/devises")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(HAL_JSON))
                .andExpect(jsonPath("$.code", equalTo(entt.getCode())))
                .andExpect(jsonPath("$.designation", equalTo(entt.getDesignation())))
                // Vérifier que les dates et auteur de Création ont été renseignés
                .andExpect(jsonPath("$.auteurCre", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.dateCre", is(notNullValue())));

        devise = deviseRepo.findByCodeAndDateEffet("TT2", today.plusDays(9));
        assertThat(devise).isNotNull();
    }

    /**
     * Test de la création d'une Devise. Cas ou il en existe une avec la même période
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotCreateDevise_WithSameDates() throws Exception {

        // Nouvelle devise, sans possibilité de contrainte d'unicité
        final Devise entt = new Devise();
        entt.setCode("XPF");
        entt.setDesignation("Franc Pacifique");
        entt.setDateEffet(today.plusYears(1));
        entt.setDateFinEffet(today.plusYears(2));

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/devises")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test de la création d'une Devise. Cas où pour une devise avec une date d'effet différentes des autres mais sans
     * date de fin (doit fail à cause des devises futures)
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotCreateDevise_WithNoDateFin() throws Exception {

        deviseLoader.createDevise("TT3", "Devise de test3", today.plusYears(50), today.plusYears(52));

        final Devise entt = new Devise();
        entt.setCode("TT3");
        entt.setDesignation("Devise de test3");
        entt.setDateEffet(today.plusYears(49));

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/devises")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());

    }

    /**
     * Test de la création d'une Devise. Cas où pour une devise avec une date d'effet identique à une autre et sans date
     * de fin (doit fail à cause des devises futures)
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotCreateDevise_WithNoDateFinAndSameDateEffet() throws Exception {

        deviseLoader.createDevise("TT4", "Devise de test4", today.plusYears(50), today.plusYears(52));

        final Devise entt = new Devise();
        entt.setCode("TT4");
        entt.setDesignation("Devise de test4");
        entt.setDateEffet(today.plusYears(50));

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/devises")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());

    }

    /**
     * Test de la création d'une Devise. Cas où pour une même devise
     *
     * dateJour < dateEffet1 < dateEffet2 < dateFin1 < DateFin2
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotCreateDevise_DateEffetBeforeDateFinBetween() throws Exception {

        deviseLoader.createDevise("TT5", "Devise de test5", today.plusDays(5), today.plusDays(10));

        final Devise entt = new Devise();
        entt.setCode("TT5");
        entt.setDesignation("Devise de test5");
        entt.setDateEffet(today.plusDays(4));
        entt.setDateFinEffet(today.plusDays(8));

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/devises")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());

    }

    /**
     * Test de la création d'une Devise. Cas où pour une même devise
     *
     * dateJour < dateEffet2 < dateEffet1 < dateFin2 < DateFin1
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotCreateDevise_DateEffetBetweenDateFinAfter() throws Exception {

        deviseLoader.createDevise("TT6", "Devise de tes6", today.plusDays(5), today.plusDays(10));

        final Devise entt = new Devise();
        entt.setCode("TT6");
        entt.setDesignation("Devise de test6");
        entt.setDateEffet(today.plusDays(8));
        entt.setDateFinEffet(today.plusDays(12));

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/devises")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());

    }

    /**
     * Test de la création d'une Devise. Cas où pour une même devise
     *
     * dateJour < dateEffet2 < dateEffet1 < dateFin2 < DateFin1
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotCreateDevise_DateEffetBetweenDateFinBetween() throws Exception {

        deviseLoader.createDevise("TT7", "Devise de test7", today.plusDays(5), today.plusDays(10));

        final Devise entt = new Devise();
        entt.setCode("TT7");
        entt.setDesignation("Devise de test7");
        entt.setDateEffet(today.plusDays(6));
        entt.setDateFinEffet(today.plusDays(9));

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/devises")
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
    private static String prepareJSON(final Devise ent) throws Exception {

        String json = JsonParser.asJsonString(ent);
        if (ent.getTauxChanges() != null && !ent.getTauxChanges().isEmpty()) {
            String jsonTx = "";
            int cpt = 0;
            for (TauxChange tx : ent.getTauxChanges()) {
                if (cpt == 0) {
                    cpt++;
                    jsonTx += JsonParser.asJsonString(tx);

                } else {
                    jsonTx += "," + JsonParser.asJsonString(tx);
                }
            }
            json = json.replaceFirst("\"tauxChanges\":\\[\\{\\},\\{\\}\\]",
                    "\"tauxChanges\":\\[" + jsonTx + "\\]");
        }
        return json;
    }
}
