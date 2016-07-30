package nc.gouv.drdnc.ilda.metier.referentiel.unite;

import static nc.gouv.drdnc.ilda.utils.ConstantesTest.HAL_JSON;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import nc.gouv.drdnc.ilda.IldaApplication;
import nc.gouv.drdnc.ilda.loader.metier.referentiel.unite.UniteLoader;
import nc.gouv.dtsi.etudes.commons.utils.Constantes;
import nc.gouv.dtsi.etudes.commons.utils.JsonParser;

/**
 * Tests d'intégration de la classe Unite
 *
 * @author ILDA
 */
@SpringApplicationConfiguration(classes = IldaApplication.class)
@WebIntegrationTest
public class UniteTestIT extends AbstractTestNGSpringContextTests {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UniteRepository uniteRepo;

    @Autowired
    private UniteLoader uniteLoader;

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
     * Test de la recherche des Unites valides
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldReturnAllValidUnites() throws Exception {

        int size = uniteRepo.findValid().size();

        mockMvc.perform(get("http://localhost/api/unites/search/valid"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(HAL_JSON))
                .andExpect(jsonPath("$._embedded.unites", hasSize(size)));
    }

    /**
     * Test de la création d'une Unité. Cas passant
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldCreateUnite_NoDates() throws Exception {

        // Nouvelle unité, sans possibilité de contrainte d'unicité
        final Unite entt = new Unite();
        entt.setCode("KW");
        entt.setDesignation("Kilo Watt");
        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/unites")
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
     * Test de la création d'une Unité. Cas passant
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldCreateUnite_WithDates() throws Exception {

        // Unité existante, mais pas sur cette période
        final Unite entt = new Unite();
        entt.setCode("KW1");
        entt.setDesignation("Kilo Watt 2");
        entt.setDateEffet(LocalDate.of(2030, 1, 1));
        entt.setDateFinEffet(LocalDate.of(2030, 12, 31));
        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/unites")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(HAL_JSON))
                .andExpect(jsonPath("$.code", equalTo(entt.getCode())))
                .andExpect(jsonPath("$.designation", equalTo(entt.getDesignation())));
    }

    /**
     * Test de la création d'une Unité. Unicité sur période
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotCreateUnite_Uncite1() throws Exception {

        // Une unité ayant le même code couvre la période 2017 -> chevauchement
        uniteLoader.createUnite("KG-1", "Test", today.plusMonths(1), today.plusMonths(12));

        final Unite entt = new Unite();
        entt.setCode("KG-1");
        entt.setDesignation("Test");
        entt.setDateEffet(today.plusMonths(6));
        entt.setDateFinEffet(today.plusMonths(18));

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/unites")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test de la création d'une Unité. Unicité sur période
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotCreateUnite_Uncite2() throws Exception {

        // Cette nouvelle unité est à cheval sur 2 autres, existantes
        final Unite entt = new Unite();
        entt.setCode("KG");
        entt.setDesignation("Kilogramme");
        entt.setDateEffet(LocalDate.of(2015, 6, 1));
        entt.setDateFinEffet(LocalDate.of(2016, 5, 31));

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/unites")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test de la création d'une Unité. Unicité sur période
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotCreateUnite_Uncite3() throws Exception {

        // La période de cette unité est incluse dans la période d'une unité existante, avec le même code
        final Unite entt = new Unite();
        entt.setCode("KG");
        entt.setDesignation("Kilogramme");
        entt.setDateEffet(LocalDate.of(2015, 3, 1));
        entt.setDateFinEffet(LocalDate.of(2015, 3, 31));

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/unites")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test de la création d'une Unité. Unicité sur période
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotCreateUnite_Uncite4() throws Exception {

        // La période de cette unité recouvre partiellement la période d'une autre
        final Unite entt = new Unite();
        entt.setCode("KG");
        entt.setDesignation("Kilogramme");
        entt.setDateEffet(LocalDate.of(2034, 3, 1));
        entt.setDateFinEffet(LocalDate.of(2035, 3, 31));

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/unites")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test de la création d'une Unité. Unicité sur période
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotCreateUnite_Uncite5() throws Exception {

        // Il existe déjà une unité sans date de fin
        final Unite entt = new Unite();
        entt.setCode("KG");
        entt.setDesignation("Kilogramme");
        entt.setDateEffet(LocalDate.of(2034, 1, 1));
        entt.setDateFinEffet(null);

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/unites")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test de la création d'une Unité. Unicité sur période
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotCreateUnite_Uncite6() throws Exception {

        // Unité existante dans le futur
        uniteLoader.createUnite("UF007", "Unité fantaisiste 7", LocalDate.of(2040, 1, 1), LocalDate.of(2040, 12, 31));

        // Création d'une entité dans le passé, sans date de fin
        final Unite entt = new Unite();
        entt.setCode("UF007");
        entt.setDesignation("Unité fantaisiste 7");
        entt.setDateEffet(LocalDate.of(2034, 3, 1));
        entt.setDateFinEffet(null);

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/unites")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test de la création d'une Unité. Unicité sur période
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotCreateUnite_Uncite7() throws Exception {

        // RG : Une seule entité peut posséder une date d'effet vide
        uniteLoader.createUnite("UF008", "Unité fantaisiste 8", null, null);

        final Unite entt = new Unite();
        entt.setCode("UF008");
        entt.setDesignation("Unité fantaisiste 8");
        entt.setDateEffet(null);
        entt.setDateFinEffet(null);

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/unites")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test de la création d'une Unité. Unicité sur période
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotCreateUnite_Uncite8() throws Exception {

        // RG : Une seule entité peut posséder une date d'effet supérieure à la date du jour
        uniteLoader.createUnite("UF009", "Unité fantaisiste 9", today.plusYears(1), today.plusYears(2));

        final Unite entt = new Unite();
        entt.setCode("UF009");
        entt.setDesignation("Unité fantaisiste 9");
        entt.setDateEffet(today.plusMonths(1));
        entt.setDateFinEffet(today.plusMonths(2));

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/unites")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test de la création d'une Unité. La date de fin doit être supérieure à la date d'effet
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotCreateUnite_DateFin() throws Exception {

        // La date de fin doit être supérieure à la date d'effet
        final Unite entt = new Unite();
        entt.setCode("KG");
        entt.setDesignation("Kilogramme");
        entt.setDateEffet(LocalDate.of(2034, 1, 1));
        entt.setDateFinEffet(LocalDate.of(2033, 12, 31));

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/unites")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test de la création d'une Unité. La date d'effet est obligatoire
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotCreateUnite_DateEffet() throws Exception {

        // La date d'effet est obligatoire
        final Unite entt = new Unite();
        entt.setCode("KG");
        entt.setDesignation("Kilogramme");
        entt.setDateEffet(null);
        entt.setDateFinEffet(LocalDate.of(2033, 12, 31));

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/unites")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test de la création d'une Unité. La date d'effet doit être strictement supérieure à la date du jour
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotCreateUnite_DateEffetStrictFuture1() throws Exception {

        // La date d'effet doit être strictement supérieure à la date du jour
        final Unite entt = new Unite();
        entt.setCode("MJ");
        entt.setDesignation("MilliJoules");
        entt.setDateEffet(today);
        entt.setDateFinEffet(null);

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/unites")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test de la création d'une Unité. La date d'effet doit être strictement supérieure à la date du jour
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotCreateUnite_DateEffetStrictFuture2() throws Exception {

        // La date d'effet doit être strictement supérieure à la date du jour
        final Unite entt = new Unite();
        entt.setCode("J");
        entt.setDesignation("Joules");
        entt.setDateEffet(today.minusDays(1));
        entt.setDateFinEffet(null);

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/unites")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test de la création d'une Unité. La date d'effet doit être strictement supérieure à la date du jour
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldCreateUnite_DateEffetStrictFuture() throws Exception {

        // La date d'effet doit être strictement supérieure à la date du jour
        final Unite entt = new Unite();
        entt.setCode("KJ");
        entt.setDesignation("KiloJoules");
        entt.setDateEffet(today.plusDays(1));
        entt.setDateFinEffet(null);

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/unites")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isCreated());
    }

    /**
     * Test de la modification d'une Unité.
     * <p>
     * Date d'effet et date de fin d'effet sont modifiables tant qu'elle sont strictement supérieures à la date du jour
     * ou nulles
     * </p>
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldUpdateUnite_Future() throws Exception {

        // Créer une entité en BDD avec date d'effet et date fin d'effet dans le futur
        Unite entt = uniteLoader.createUnite("MM", "MilliMètre", LocalDate.of(2030, 1, 1), LocalDate.of(2030, 12, 31));
        // On peut tout modifier
        entt.setDesignation("MilliMètre modifié");
        entt.setDateEffet(LocalDate.of(2031, 1, 1));
        entt.setDateFinEffet(LocalDate.of(2031, 12, 31));

        String json = prepareJSON(entt);

        mockMvc.perform(put("http://localhost/api/unites/" + entt.getId())
                .contentType(HAL_JSON)
                .content(json))
                .andExpect(status().isNoContent());
    }

    /**
     * Test de la modification d'une Unité.
     * <p>
     * Date d'effet et date de fin d'effet sont modifiables tant qu'elle sont strictement supérieures à la date du jour
     * ou nulles
     * </p>
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldUpdateUnite_NoDates() throws Exception {

        // Créer une entité en BDD avec date d'effet et date fin d'effet dans le futur
        Unite entt = uniteLoader.createUnite("DM", "DeciMètre", null, null);
        // On peut tout modifier
        entt.setDesignation("DeciMètre modifié");
        entt.setDateEffet(LocalDate.of(2031, 1, 1));
        entt.setDateFinEffet(LocalDate.of(2031, 12, 31));

        String json = prepareJSON(entt);

        mockMvc.perform(put("http://localhost/api/unites/" + entt.getId())
                .contentType(HAL_JSON)
                .content(json))
                .andExpect(status().isNoContent());
    }

    /**
     * Test de la modification d'une Unité.
     * <p>
     * Date d'effet et date de fin d'effet sont modifiables tant qu'elle sont strictement supérieures à la date du jour
     * ou nulles
     * </p>
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldUpdateUnite_Past() throws Exception {

        // Créer une entité en BDD avec date d'effet dans le passé
        Unite entt = uniteLoader.createUnite("M5", "Mètre 5 dimensions", LocalDate.of(2016, 1, 1), null);
        // Ne modifier que la date de fin d'effet
        entt.setDateFinEffet(LocalDate.of(2030, 12, 31));

        String json = prepareJSON(entt);

        mockMvc.perform(put("http://localhost/api/unites/" + entt.getId())
                .contentType(HAL_JSON)
                .content(json))
                .andExpect(status().isNoContent());
    }

    /**
     * Test de la modification d'une Unité. Modification de champs interdite
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotUpdateUnite_Past() throws Exception {

        // Créer une entité en BDD avec date d'effet dans le passé
        Unite entt = uniteLoader.createUnite("M4", "Mètre 4 dimensions", LocalDate.of(2016, 1, 1), null);
        // Modifier un champ autre que la date de fin d'effet
        entt.setDesignation("Mètre 4-dimensions");

        String json = prepareJSON(entt);

        mockMvc.perform(put("http://localhost/api/unites/" + entt.getId())
                .contentType(HAL_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test de la modification d'une Unité. Modification avant sa date d'effet
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldUpdateUnite_BeforeDate() throws Exception {

        // Créer une entité en BDD avec date d'effet dans le futur
        Unite entt = uniteLoader.createUnite("UF1", "Unité fantaisiste 1", LocalDate.of(2036, 1, 1), LocalDate.of(2036, 12, 31));
        // Modifier dates d'effet et fin d'effet
        entt.setDateEffet(LocalDate.of(2037, 1, 1));
        entt.setDateFinEffet(LocalDate.of(2037, 12, 31));

        String json = prepareJSON(entt);

        mockMvc.perform(put("http://localhost/api/unites/" + entt.getId())
                .contentType(HAL_JSON)
                .content(json))
                .andExpect(status().isNoContent());
    }

    /**
     * Test de la modification d'une Unité. Modification avec un date d'effet dans le passé
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotUpdateUnite_BeforeDate() throws Exception {

        // Créer une entité en BDD avec date d'effet dans le futur
        Unite entt = uniteLoader.createUnite("UF2", "Unité fantaisiste 2", LocalDate.of(2036, 1, 1), LocalDate.of(2036, 12, 31));
        // Modifier dates d'effet et fin d'effet : date d'effet dans le passé
        entt.setDateEffet(LocalDate.of(2016, 1, 1));
        entt.setDateFinEffet(LocalDate.of(2037, 12, 31));

        String json = prepareJSON(entt);

        mockMvc.perform(put("http://localhost/api/unites/" + entt.getId())
                .contentType(HAL_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test de la suppresion d'une Unité. Cas Passant
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldDeleteUnite() throws Exception {

        Unite entt = uniteLoader.createUnite("M", "Mètre", LocalDate.of(2033, 1, 1), LocalDate.of(2033, 12, 31));

        mockMvc.perform(delete("http://localhost/api/unites/" + entt.getId()))
                .andExpect(status().isNoContent());
    }

    /**
     * Test de la suppresion d'une Unité. La date d'effet inférieure à la date du jour
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotDeleteUnite_DateEffet() throws Exception {

        Unite entt = uniteLoader.createUnite("M", "Mètre", LocalDate.of(2013, 1, 1), LocalDate.of(2013, 12, 31));

        mockMvc.perform(delete("http://localhost/api/unites/" + entt.getId()))
                .andExpect(status().isBadRequest());
    }

    /**
     * Préparation du JSON au format HAL+JSON
     *
     * @param ent une entité à transformer
     * @return JSON de l'entité, au format HAL+JSON
     * @throws Exception en cas d'erreur de parsing
     */
    private static String prepareJSON(final Unite ent) throws Exception {

        String json = JsonParser.asJsonString(ent);
        // Nothing else to do
        return json;
    }
}
