package nc.gouv.drdnc.ilda.metier.referentiel.incoterm;

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
import nc.gouv.drdnc.ilda.loader.metier.referentiel.incoterm.IncotermLoader;
import nc.gouv.dtsi.etudes.commons.utils.Constantes;
import nc.gouv.dtsi.etudes.commons.utils.JsonParser;

/**
 * Tests d'intégration de la classe Incoterm
 *
 * @author ILDAl
 */
@SpringApplicationConfiguration(classes = IldaApplication.class)
@WebIntegrationTest
public class IncotermTestIT extends AbstractTestNGSpringContextTests {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private IncotermRepository incotermRepository;

    @Autowired
    private IncotermLoader incotermLoader;

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
     * Test de la recherche des Incoterms valides
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldReturnAllValidIncoterms() throws Exception {

        int size = incotermRepository.findValid().size();

        mockMvc.perform(get("http://localhost/api/incoterms/search/valid"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(HAL_JSON))
                .andExpect(jsonPath("$._embedded.incoterms", hasSize(size)));
    }

    /**
     * Test de la création d'une incoterm. Cas passant
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldCreateIncoterm_NoDates() throws Exception {

        // Nouvelle incoterm, sans possibilité de contrainte d'unicité
        final Incoterm entt = new Incoterm();
        entt.setCode("DAF");
        entt.setDesignation("Rendu frontiere");
        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/incoterms")
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
     * Test de la création d'une incoterm. Cas passant
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldCreateIncoterm_WithDates() throws Exception {

        // incoterm existante, mais pas sur cette période
        final Incoterm entt = new Incoterm();
        entt.setCode("DDP");
        entt.setDesignation("Rendu droits acquittes");
        entt.setDateEffet(LocalDate.of(2030, 1, 1));
        entt.setMotifEffet("Motif effet DDP");
        entt.setDateFinEffet(LocalDate.of(2030, 12, 31));
        entt.setMotifFinEffet("Motif fin effet DDP");
        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/incoterms")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(HAL_JSON))
                .andExpect(jsonPath("$.code", equalTo(entt.getCode())))
                .andExpect(jsonPath("$.designation", equalTo(entt.getDesignation())))
                .andExpect(jsonPath("$.motifEffet", equalTo(entt.getMotifEffet())))
                .andExpect(jsonPath("$.motifFinEffet", equalTo(entt.getMotifFinEffet())));
    }

    /**
     * Test de la création d'une incoterm. Unicité sur période
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotCreateIncoterm_Uncite1() throws Exception {

        // Chevauchement de deux enités sur une période
        incotermLoader.createIncoterm("AA1", "Test", today.plusMonths(1), "Motif", today.plusMonths(12), "Motif");

        final Incoterm entt = new Incoterm();
        entt.setCode("AA1");
        entt.setDesignation("Test");
        entt.setDateEffet(today.plusMonths(6));
        entt.setMotifEffet("Motif");
        entt.setDateFinEffet(today.plusMonths(18));
        entt.setMotifFinEffet("Motif");

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/incoterms")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test de la création d'une incoterm. Unicité sur période
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotCreateIncoterm_Uncite2() throws Exception {

        // Cette nouvelle incoterm est à cheval sur 2 autres, existantes
        final Incoterm entt = new Incoterm();
        entt.setCode(IncotermLoader.DES_CODE);
        entt.setDesignation(IncotermLoader.FOB_LIB);
        entt.setDateEffet(LocalDate.of(2015, 6, 1));
        entt.setMotifEffet(IncotermLoader.FOB_MOTIF_EFFET);
        entt.setDateFinEffet(LocalDate.of(2016, 5, 31));
        entt.setMotifFinEffet(IncotermLoader.FOB_MOTIF_FIN_EFFET);

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/incoterms")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test de la création d'une incoterm. Unicité sur période
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotCreateIncoterm_Uncite3() throws Exception {

        // La période de cette incoterm est incluse dans la période d'une
        // incoterm existante, avec le même code
        final Incoterm entt = new Incoterm();
        entt.setCode(IncotermLoader.FCA_CODE);
        entt.setDesignation(IncotermLoader.FCA_LIB);
        entt.setDateEffet(LocalDate.of(2015, 3, 1));
        entt.setMotifEffet(IncotermLoader.FCA_MOTIF_EFFET);
        entt.setDateFinEffet(LocalDate.of(2015, 3, 31));
        entt.setMotifFinEffet(IncotermLoader.FCA_MOTIF_FIN_EFFET);

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/incoterms")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test de la création d'une incoterm. Unicité sur période
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotCreateIncoterm_Uncite4() throws Exception {

        // La période de cette incoterm recouvre partiellement la période d'une
        // autre
        final Incoterm entt = new Incoterm();
        entt.setCode(IncotermLoader.FCA_CODE);
        entt.setDesignation(IncotermLoader.FCA_LIB);
        entt.setDateEffet(LocalDate.of(2034, 3, 1));
        entt.setMotifEffet(IncotermLoader.FCA_MOTIF_EFFET);
        entt.setDateFinEffet(LocalDate.of(2035, 3, 31));
        entt.setMotifFinEffet(IncotermLoader.FCA_MOTIF_FIN_EFFET);

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/incoterms")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test de la création d'une incoterm. Unicité sur période
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotCreateIncoterm_Uncite5() throws Exception {

        // Il existe déjà une incoterm sans date de fin
        final Incoterm entt = new Incoterm();
        entt.setCode(IncotermLoader.FOB_CODE);
        entt.setDesignation(IncotermLoader.FOB_LIB);
        entt.setDateEffet(LocalDate.of(2034, 1, 1));
        entt.setMotifEffet(IncotermLoader.FOB_MOTIF_EFFET);
        entt.setDateFinEffet(null);
        entt.setMotifFinEffet(null);

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/incoterms")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test de la création d'une incoterm. Unicité sur période
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotCreateIncoterm_Uncite6() throws Exception {

        // RG : Une seule entité peut posséder une date d'effet vide
        incotermLoader.createIncoterm("AA6", "Test", null, null, null, null);

        final Incoterm entt = new Incoterm();
        entt.setCode("AA6");
        entt.setDesignation("Test");
        entt.setDateEffet(null);
        entt.setMotifEffet(null);
        entt.setDateFinEffet(null);
        entt.setMotifFinEffet(null);

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/incoterms")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test de la création d'une incoterm. Unicité sur période
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotCreateIncoterm_Uncite7() throws Exception {

        // RG : Une seule entité peut posséder une date d'effet supérieure à la date du jour
        incotermLoader.createIncoterm("AA7", "Test", today.plusYears(1), "Motif", today.plusYears(2), "Motif");

        final Incoterm entt = new Incoterm();
        entt.setCode("AA7");
        entt.setDesignation("Test");
        entt.setDateEffet(today.plusMonths(1));
        entt.setMotifEffet("Motif");
        entt.setDateFinEffet(today.plusMonths(2));
        entt.setMotifFinEffet("Motif");

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/incoterms")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test de la création d'une incoterm. La date de fin doit être supérieure à la date d'effet
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotCreateIncoterm_DateFin() throws Exception {

        // La date de fin doit être supérieure à la date d'effet
        final Incoterm entt = new Incoterm();
        entt.setCode(IncotermLoader.FOB_CODE);
        entt.setDesignation(IncotermLoader.FOB_LIB);
        entt.setDateEffet(LocalDate.of(2034, 1, 1));
        entt.setMotifEffet(IncotermLoader.FOB_MOTIF_EFFET);
        entt.setDateFinEffet(LocalDate.of(2033, 12, 31));
        entt.setMotifFinEffet(IncotermLoader.FOB_MOTIF_FIN_EFFET);

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/incoterms")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test de la création d'une incoterm. La date d'effet est obligatoire
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotCreateIncoterm_DateEffet() throws Exception {

        // La date d'effet est obligatoire
        final Incoterm entt = new Incoterm();
        entt.setCode(IncotermLoader.FOB_CODE);
        entt.setDesignation(IncotermLoader.FOB_LIB);
        entt.setDateEffet(null);
        entt.setMotifEffet(null);
        entt.setDateFinEffet(LocalDate.of(2033, 12, 31));
        entt.setMotifFinEffet(IncotermLoader.FOB_MOTIF_FIN_EFFET);

        String jsonCreate = prepareJSON(entt);

        mockMvc.perform(post("http://localhost/api/incoterms")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());
    }

    /**
     * l Test de la modification d'une incoterm. Cas Passant
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldUpdateIncoterm_Future() throws Exception {

        // Créer une entité en BDD avec date d'effet dans le futur
        Incoterm entt = incotermLoader.createIncoterm("CFR", "Cout et fret", LocalDate.of(2030, 1, 1),
                "Motif effet CFR", null, null);
        // On peut tout modifier
        entt.setDesignation("Cout et fret modifié");
        entt.setDateEffet(LocalDate.of(2031, 1, 1));
        entt.setMotifEffet("Motif effet CFR modifie");
        entt.setDateFinEffet(LocalDate.of(2031, 12, 31));
        entt.setMotifFinEffet("Motif fin effet CFR modifie");

        String json = prepareJSON(entt);

        mockMvc.perform(put("http://localhost/api/incoterms/" + entt.getId()).contentType(HAL_JSON).content(json))
                .andExpect(status().isNoContent());
    }

    /**
     * Test de la modification d'une incoterm. Cas Passant
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldUpdateIncoterm_Past() throws Exception {

        // Créer une entité en BDD avec date d'effet dans le passé
        Incoterm entt = incotermLoader.createIncoterm("CIF", "Cout, assurance, fret", LocalDate.of(2030, 1, 1),
                "Motif effet CIF", null, null);
        // On peut tout modifier
        entt.setDateFinEffet(LocalDate.of(2031, 12, 31));
        entt.setMotifFinEffet("Motif fin effet CIF modifie");

        String json = prepareJSON(entt);

        mockMvc.perform(put("http://localhost/api/incoterms/" + entt.getId())
                .contentType(HAL_JSON)
                .content(json))
                .andExpect(status().isNoContent());
    }

    /**
     * Test de la modification d'une incoterm. Modification de champs interdite
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotUpdateIncoterm_Past() throws Exception {

        // Créer une entité en BDD avec date d'effet dans le passé
        Incoterm entt = incotermLoader.createIncoterm("DDP", "Rendu droits acquittes", LocalDate.of(2016, 1, 1),
                "Motif effet DDP", null, null);
        // Modifier un champ autre que la date de fin d'effet
        entt.setDesignation("Rendu droits acquittes modifie");

        String json = prepareJSON(entt);

        mockMvc.perform(put("http://localhost/api/incoterms/" + entt.getId())
                .contentType(HAL_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test de la suppresion d'une incoterm. Cas Passant
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldDeleteIncoterm() throws Exception {

        Incoterm entt = incotermLoader.createIncoterm("DDP", "Rendu droits acquittes", LocalDate.of(2033, 1, 1),
                "Motif effet DDP", LocalDate.of(2033, 12, 31), "Motif fin effet DDP");

        mockMvc.perform(delete("http://localhost/api/incoterms/" + entt.getId()))
                .andExpect(status().isNoContent());
    }

    /**
     * Test de la suppresion d'une incoterm. La date d'effet inférieure à la date du jour
     *
     * @throws Exception Exception
     */
    @Test
    public void shouldNotDeleteIncoterm_DateEffet() throws Exception {

        Incoterm entt = incotermLoader.createIncoterm("DDP", "Rendu droits acquittes", LocalDate.of(2013, 1, 1),
                "Motif effet DDP", LocalDate.of(2013, 12, 31), "Motif fin effet DDP");

        mockMvc.perform(delete("http://localhost/api/incoterms/" + entt.getId()))
                .andExpect(status().isBadRequest());
    }

    /**
     * Préparation du JSON au format HAL+JSON
     *
     * @param ent une entité à transformer
     * @return JSON de l'entité, au format HAL+JSON
     * @throws Exception en cas d'erreur de parsing
     */
    private static String prepareJSON(final Incoterm ent) throws Exception {

        String json = JsonParser.asJsonString(ent);
        // Nothing else to do
        return json;
    }
}
