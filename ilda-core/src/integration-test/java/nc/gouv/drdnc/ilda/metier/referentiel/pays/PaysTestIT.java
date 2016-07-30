package nc.gouv.drdnc.ilda.metier.referentiel.pays;

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
import nc.gouv.drdnc.ilda.loader.metier.referentiel.pays.PaysLoader;
import nc.gouv.dtsi.etudes.commons.utils.Constantes;
import nc.gouv.dtsi.etudes.commons.utils.JsonParser;

/**
 * Tests d'intégration de la classe Pays
 *
 * @author ILDA
 */
@SpringApplicationConfiguration(classes = IldaApplication.class)
@WebIntegrationTest
public class PaysTestIT extends AbstractTestNGSpringContextTests {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private PaysRepository paysRepo;

    @Autowired
	private PaysLoader paysLoader;

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
	 * Test de la recherche des Pays valides
	 *
	 * @throws Exception
	 *             Exception
	 */
    @Test
	public void shouldReturnAllValidPays() throws Exception {

		int size = paysRepo.findValid().size();

		mockMvc.perform(get("http://localhost/api/pays/search/valid"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(HAL_JSON))
				.andExpect(jsonPath("$._embedded.pays", hasSize(size)));
    }

    /**
	 * Test de la création d'un Pays. Cas passant
	 *
	 * @throws Exception
	 *             Exception
	 */
    @Test
	public void shouldCreatePays_NoDates() throws Exception {

		// Nouveau pays, sans possibilité de contrainte d'unicité
		final Pays entt = new Pays();
		entt.setCode2("EG");
		entt.setCode3("EGY");
		entt.setDesignation("Egypte");
        String jsonCreate = prepareJSON(entt);

		mockMvc.perform(post("http://localhost/api/pays")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(HAL_JSON))
				.andExpect(jsonPath("$.code2", equalTo(entt.getCode2())))
				.andExpect(jsonPath("$.code3", equalTo(entt.getCode3())))
                .andExpect(jsonPath("$.designation", equalTo(entt.getDesignation())))
                // Vérifier que les dates et auteur de Création ont été renseignés
                .andExpect(jsonPath("$.auteurCre", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.dateCre", is(notNullValue())));
    }

    /**
	 * Test de la création d'un Pays. Cas passant
	 *
	 * @throws Exception
	 *             Exception
	 */
    @Test
	public void shouldCreatePays_WithDates() throws Exception {

		// Pays existant, mais pas sur cette période
		final Pays entt = new Pays();
        entt.setCode2("A1");
        entt.setCode3("AA1");
        entt.setDesignation("Pays Test 1");
        entt.setDateEffet(LocalDate.of(2030, 1, 1));
        entt.setDateFinEffet(LocalDate.of(2030, 12, 31));
        String jsonCreate = prepareJSON(entt);

		mockMvc.perform(post("http://localhost/api/pays")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(HAL_JSON))
				.andExpect(jsonPath("$.code2", equalTo(entt.getCode2())))
				.andExpect(jsonPath("$.code3", equalTo(entt.getCode3())))
                .andExpect(jsonPath("$.designation", equalTo(entt.getDesignation())));
    }

    /**
	 * Test de la création d'un Pays. Unicité Code 2 sur période
	 *
	 * @throws Exception
	 *             Exception
	 */
    @Test
	public void shouldNotCreatePays_UniciteCode2_1() throws Exception {

		// Un pays ayant le même code2 couvre la période 2017 -> chevauchement
		final Pays entt = new Pays();
		entt.setCode2("CA");
		entt.setCode3("CAD");
		entt.setDesignation("Canada");
		entt.setDateEffet(LocalDate.of(2017, 6, 1));
		entt.setDateFinEffet(LocalDate.of(2018, 5, 31));

		String jsonCreate = prepareJSON(entt);

		mockMvc.perform(post("http://localhost/api/pays").contentType(HAL_JSON).content(jsonCreate))
				.andExpect(status().isBadRequest());
	}

	/**
	 * Test de la création d'un Pays. Unicité Code 3 sur période
	 *
	 * @throws Exception
	 *             Exception
	 */
	@Test
	public void shouldNotCreatePays_UniciteCode3_1() throws Exception {

		// Un pays ayant le même code3 couvre la période 2017 -> chevauchement
		final Pays entt = new Pays();
		entt.setCode2("CN");
		entt.setCode3("CAN");
		entt.setDesignation("Canada");
        entt.setDateEffet(LocalDate.of(2017, 6, 1));
        entt.setDateFinEffet(LocalDate.of(2018, 5, 31));

        String jsonCreate = prepareJSON(entt);

		mockMvc.perform(post("http://localhost/api/pays")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());
    }

    /**
	 * Test de la création d'un Pays. Unicité Code 2 sur période
	 *
	 * @throws Exception
	 *             Exception
	 */
    @Test
	public void shouldNotCreatePays_UniciteCode2_2() throws Exception {

		// Ce nouveau pays est à cheval sur 2 autres, existants
		final Pays entt = new Pays();
		entt.setCode2("CA");
		entt.setCode3("CAD");
		entt.setDesignation("Canada");
		entt.setDateEffet(LocalDate.of(2015, 6, 1));
		entt.setDateFinEffet(LocalDate.of(2016, 5, 31));

		String jsonCreate = prepareJSON(entt);

		mockMvc.perform(post("http://localhost/api/pays").contentType(HAL_JSON).content(jsonCreate))
				.andExpect(status().isBadRequest());
	}

	/**
	 * Test de la création d'un Pays. Unicité Code 3 sur période
	 *
	 * @throws Exception
	 *             Exception
	 */
	@Test
	public void shouldNotCreatePays_UniciteCode3_2() throws Exception {

		// Ce nouveau pays est à cheval sur 2 autres, existants
		final Pays entt = new Pays();
		entt.setCode2("CN");
		entt.setCode3("CAN");
		entt.setDesignation("Canada");
        entt.setDateEffet(LocalDate.of(2015, 6, 1));
        entt.setDateFinEffet(LocalDate.of(2016, 5, 31));

        String jsonCreate = prepareJSON(entt);

		mockMvc.perform(post("http://localhost/api/pays")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());
    }

    /**
	 * Test de la création d'un Pays. Unicité Code 2 sur période
	 *
	 * @throws Exception
	 *             Exception
	 */
    @Test
	public void shouldNotCreatePays_UniciteCode2_3() throws Exception {

		// La période de ce pays est incluse dans la période d'un pays existant,
		// avec le même code2
		final Pays entt = new Pays();
		entt.setCode2("CA");
		entt.setCode3("CAD");
		entt.setDesignation("Canada");
		entt.setDateEffet(LocalDate.of(2015, 3, 1));
		entt.setDateFinEffet(LocalDate.of(2015, 3, 31));

		String jsonCreate = prepareJSON(entt);

		mockMvc.perform(post("http://localhost/api/pays").contentType(HAL_JSON).content(jsonCreate))
				.andExpect(status().isBadRequest());
	}

	/**
	 * Test de la création d'un Pays. Unicité Code 2 sur période
	 *
	 * @throws Exception
	 *             Exception
	 */
	@Test
	public void shouldNotCreatePays_UniciteCode3_3() throws Exception {

		// La période de ce pays est incluse dans la période d'un pays existant,
		// avec le même code3
		final Pays entt = new Pays();
		entt.setCode2("CN");
		entt.setCode3("CAN");
		entt.setDesignation("Canada");
        entt.setDateEffet(LocalDate.of(2015, 3, 1));
        entt.setDateFinEffet(LocalDate.of(2015, 3, 31));

        String jsonCreate = prepareJSON(entt);

		mockMvc.perform(post("http://localhost/api/pays")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());
    }

    /**
	 * Test de la création d'un Pays. Unicité Code 2 sur période
	 *
	 * @throws Exception
	 *             Exception
	 */
    @Test
	public void shouldNotCreatePays_Code2_4() throws Exception {

        // La période de cette unité recouvre partiellement la période d'une autre
		final Pays entt = new Pays();
		entt.setCode2("CA");
		entt.setCode3("CAD");
		entt.setDesignation("Canada");
		entt.setDateEffet(LocalDate.of(2034, 3, 1));
		entt.setDateFinEffet(LocalDate.of(2035, 3, 31));

		String jsonCreate = prepareJSON(entt);

		mockMvc.perform(post("http://localhost/api/pays").contentType(HAL_JSON).content(jsonCreate))
				.andExpect(status().isBadRequest());
	}

	/**
	 * Test de la création d'un Pays. Unicité Code 2 sur période
	 *
	 * @throws Exception
	 *             Exception
	 */
	@Test
	public void shouldNotCreatePays_Code3_4() throws Exception {

		// La période de cette unité recouvre partiellement la période d'une
		// autre
		final Pays entt = new Pays();
		entt.setCode2("CN");
		entt.setCode3("CAN");
		entt.setDesignation("Canada");
        entt.setDateEffet(LocalDate.of(2034, 3, 1));
        entt.setDateFinEffet(LocalDate.of(2035, 3, 31));

        String jsonCreate = prepareJSON(entt);

		mockMvc.perform(post("http://localhost/api/pays")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());
    }

    /**
	 * Test de la création d'un Pays. Unicité code 2 sur période
	 *
	 * @throws Exception
	 *             Exception
	 */
    @Test
	public void shouldNotCreatePays_Code2_5() throws Exception {

		// Il existe déjà un pays sans date de fin
		final Pays entt = new Pays();
		entt.setCode2("CA");
		entt.setCode3("CAD");
		entt.setDesignation("Canada");
		entt.setDateEffet(LocalDate.of(2034, 1, 1));
		entt.setDateFinEffet(null);

		String jsonCreate = prepareJSON(entt);

		mockMvc.perform(post("http://localhost/api/pays").contentType(HAL_JSON).content(jsonCreate))
				.andExpect(status().isBadRequest());
	}

	/**
	 * Test de la création d'un Pays. Unicité code 3 sur période
	 *
	 * @throws Exception
	 *             Exception
	 */
	@Test
	public void shouldNotCreatePays_Code3_5() throws Exception {

		// Il existe déjà un pays sans date de fin
		final Pays entt = new Pays();
		entt.setCode2("CN");
		entt.setCode3("CAN");
		entt.setDesignation("Canada");
        entt.setDateEffet(LocalDate.of(2034, 1, 1));
        entt.setDateFinEffet(null);

        String jsonCreate = prepareJSON(entt);

		mockMvc.perform(post("http://localhost/api/pays")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());
    }

    /**
	 * Test de la création d'un Pays. Unicité code 2 sur période
	 *
	 * @throws Exception
	 *             Exception
	 */
    @Test
	public void shouldNotCreatePays_Code2_6() throws Exception {

		// Pays existant dans le futur
		paysLoader.createPays("WA", "WAK", "Wakanda", LocalDate.of(2040, 1, 1), LocalDate.of(2040, 12, 31));

		// Création d'un pays dans le passé, sans date de fin
		final Pays entt = new Pays();
		entt.setCode2("WA");
		entt.setCode3("WAD");
		entt.setDesignation("Wakanda");
		entt.setDateEffet(LocalDate.of(2034, 3, 1));
		entt.setDateFinEffet(null);

		String jsonCreate = prepareJSON(entt);

		mockMvc.perform(post("http://localhost/api/pays").contentType(HAL_JSON).content(jsonCreate))
				.andExpect(status().isBadRequest());
	}

	/**
	 * Test de la création d'un Pays. Unicité code 3 sur période
	 *
	 * @throws Exception
	 *             Exception
	 */
	@Test
	public void shouldNotCreatePays_Code3_6() throws Exception {

		// Pays existant dans le futur
		paysLoader.createPays("WA", "WAK", "Wakanda", LocalDate.of(2040, 1, 1), LocalDate.of(2040, 12, 31));

		// Création d'un pays dans le passé, sans date de fin
		final Pays entt = new Pays();
		entt.setCode2("WK");
		entt.setCode3("WAK");
		entt.setDesignation("Wakanda");
        entt.setDateEffet(LocalDate.of(2034, 3, 1));
        entt.setDateFinEffet(null);

        String jsonCreate = prepareJSON(entt);

		mockMvc.perform(post("http://localhost/api/pays")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());
    }

    /**
	 * Test de la création d'un Pays. La date de fin doit être supérieure à la
	 * date d'effet
	 *
	 * @throws Exception
	 *             Exception
	 */
    @Test
	public void shouldNotCreatePays_DateFin() throws Exception {

        // La date de fin doit être supérieure à la date d'effet
		final Pays entt = new Pays();
		entt.setCode2("CA");
		entt.setCode3("CAN");
		entt.setDesignation("Canada");
        entt.setDateEffet(LocalDate.of(2034, 1, 1));
        entt.setDateFinEffet(LocalDate.of(2033, 12, 31));

        String jsonCreate = prepareJSON(entt);

		mockMvc.perform(post("http://localhost/api/pays")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());
    }

    /**
	 * Test de la création d'un Pays. La date d'effet est obligatoire
	 *
	 * @throws Exception
	 *             Exception
	 */
    @Test
	public void shouldNotCreatePays_DateEffet() throws Exception {

        // La date d'effet est obligatoire
		final Pays entt = new Pays();
		entt.setCode2("CA");
		entt.setCode3("CAN");
		entt.setDesignation("Canada");
        entt.setDateEffet(null);
        entt.setDateFinEffet(LocalDate.of(2033, 12, 31));

        String jsonCreate = prepareJSON(entt);

		mockMvc.perform(post("http://localhost/api/pays")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());
    }

    /**
	 * Test de la création d'un Pays. La date d'effet doit être strictement
	 * supérieure à la date du jour
	 *
	 * @throws Exception
	 *             Exception
	 */
    @Test
	public void shouldNotCreatePays_DateEffetStrictFuture1() throws Exception {

        // La date d'effet doit être strictement supérieure à la date du jour
		final Pays entt = new Pays();
		entt.setCode2("CA");
		entt.setCode3("CAN");
		entt.setDesignation("Canada");
        entt.setDateEffet(today);
        entt.setDateFinEffet(null);

        String jsonCreate = prepareJSON(entt);

		mockMvc.perform(post("http://localhost/api/pays")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());
    }

    /**
	 * Test de la création d'un Pays. La date d'effet doit être strictement
	 * supérieure à la date du jour
	 *
	 * @throws Exception
	 *             Exception
	 */
    @Test
	public void shouldNotCreatePays_DateEffetStrictFuture2() throws Exception {

        // La date d'effet doit être strictement supérieure à la date du jour
		final Pays entt = new Pays();
		entt.setCode2("CA");
		entt.setCode3("CAN");
		entt.setDesignation("Canada");
        entt.setDateEffet(today.minusDays(1));
        entt.setDateFinEffet(null);

        String jsonCreate = prepareJSON(entt);

		mockMvc.perform(post("http://localhost/api/pays")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());
    }

    /**
	 * Test de la création d'un Pays. La date d'effet doit être strictement
	 * supérieure à la date du jour
	 *
	 * @throws Exception
	 *             Exception
	 */
    @Test
	public void shouldCreatePays_DateEffetStrictFuture() throws Exception {

        // La date d'effet doit être strictement supérieure à la date du jour
		final Pays entt = new Pays();
		entt.setCode2("DV");
		entt.setCode3("DRV");
		entt.setDesignation("Druhim Vanashta");
        entt.setDateEffet(today.plusDays(1));
        entt.setDateFinEffet(null);

        String jsonCreate = prepareJSON(entt);

		mockMvc.perform(post("http://localhost/api/pays")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isCreated());
    }

    /**
	 * Test de la modification d'un Pays.
	 * <p>
	 * Date d'effet et date de fin d'effet sont modifiables tant qu'elle sont
	 * strictement supérieures à la date du jour ou nulles
	 * </p>
	 *
	 * @throws Exception
	 *             Exception
	 */
    @Test
	public void shouldUpdatePays_Future1() throws Exception {

        // Créer une entité en BDD avec date d'effet et date fin d'effet dans le futur
        Pays entt = paysLoader.createPays("A3", "AA3", "Pays Test 3", LocalDate.of(2030, 1, 1), LocalDate.of(2030, 12, 31));
        // On peut tout modifier
		entt.setDesignation("Wakanda N'Tchalla");
        entt.setDateEffet(LocalDate.of(2031, 1, 1));
        entt.setDateFinEffet(LocalDate.of(2031, 12, 31));

        String json = prepareJSON(entt);

		mockMvc.perform(put("http://localhost/api/pays/" + entt.getId())
                .contentType(HAL_JSON)
                .content(json))
                .andExpect(status().isNoContent());
    }

    /**
	 * Test de la modification d'un Pays.
	 * <p>
	 * Date d'effet et date de fin d'effet sont modifiables tant qu'elle sont
	 * strictement supérieures à la date du jour ou nulles
	 * </p>
	 *
	 * @throws Exception
	 *             Exception
	 */
    @Test
	public void shouldUpdatePays_NoDates() throws Exception {

        // Créer une entité en BDD avec date d'effet et date fin d'effet dans le futur
        Pays entt = paysLoader.createPays("A2", "AA2", "Pays Test 2", null, null);
        // On peut tout modifier
		entt.setDesignation("Latverie Von F");
        entt.setDateEffet(LocalDate.of(2031, 1, 1));
        entt.setDateFinEffet(LocalDate.of(2031, 12, 31));

        String json = prepareJSON(entt);

		mockMvc.perform(put("http://localhost/api/pays/" + entt.getId())
                .contentType(HAL_JSON)
                .content(json))
                .andExpect(status().isNoContent());
    }

    /**
	 * Test de la modification d'un Pays.
	 * <p>
	 * Date d'effet et date de fin d'effet sont modifiables tant qu'elle sont
	 * strictement supérieures à la date du jour ou nulles
	 * </p>
	 *
	 * @throws Exception
	 *             Exception
	 */
    @Test
	public void shouldUpdatePays_Past() throws Exception {

        // Créer une entité en BDD avec date d'effet dans le passé
		Pays entt = paysLoader.createPays("TH", "THE", "Themiscyra", LocalDate.of(2016, 1, 1), null);
        // Ne modifier que la date de fin d'effet
        entt.setDateFinEffet(LocalDate.of(2030, 12, 31));

        String json = prepareJSON(entt);

		mockMvc.perform(put("http://localhost/api/pays/" + entt.getId())
                .contentType(HAL_JSON)
                .content(json))
                .andExpect(status().isNoContent());
    }

    /**
	 * Test de la modification d'un Pays. Modification de champs interdite
	 *
	 * @throws Exception
	 *             Exception
	 */
    @Test
	public void shouldNotUpdatePays_Past() throws Exception {

        // Créer une entité en BDD avec date d'effet dans le passé
		Pays entt = paysLoader.createPays("AT", "ATL", "Atlantide", LocalDate.of(2016, 1, 1), null);
        // Modifier un champ autre que la date de fin d'effet
		entt.setDesignation("Mu");

        String json = prepareJSON(entt);

		mockMvc.perform(put("http://localhost/api/pays/" + entt.getId())
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
	public void shouldUpdatePays_BeforeDate() throws Exception {

        // Créer une entité en BDD avec date d'effet dans le passé
		Pays entt = paysLoader.createPays("KA", "KAN", "Kandor", LocalDate.of(2036, 1, 1), LocalDate.of(2036, 12, 31));
        // Modifier dates d'effet et fin d'effet
        entt.setDateEffet(LocalDate.of(2037, 1, 1));
        entt.setDateFinEffet(LocalDate.of(2037, 12, 31));

        String json = prepareJSON(entt);

		mockMvc.perform(put("http://localhost/api/pays/" + entt.getId())
                .contentType(HAL_JSON)
                .content(json))
                .andExpect(status().isNoContent());
    }

    /**
	 * Test de la modification d'un Pays. Modification avec un date d'effet dans
	 * le passé
	 *
	 * @throws Exception
	 *             Exception
	 */
    @Test
	public void shouldNotUpdatePays_BeforeDate() throws Exception {

        // Créer une entité en BDD avec date d'effet dans le passé
		Pays entt = paysLoader.createPays("SH", "SHA", "Shambhala", LocalDate.of(2036, 1, 1),
				LocalDate.of(2036, 12, 31));
        // Modifier dates d'effet et fin d'effet : date d'effet dans le passé
        entt.setDateEffet(LocalDate.of(2016, 1, 1));
        entt.setDateFinEffet(LocalDate.of(2037, 12, 31));

        String json = prepareJSON(entt);

		mockMvc.perform(put("http://localhost/api/pays/" + entt.getId())
                .contentType(HAL_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    /**
	 * Test de la suppression d'un Pays. Cas Passant
	 *
	 * @throws Exception
	 *             Exception
	 */
    @Test
	public void shouldDeletePays() throws Exception {

		Pays entt = paysLoader.createPays("SH", "SHA", "Shambhala", LocalDate.of(2033, 1, 1),
				LocalDate.of(2033, 12, 31));

		mockMvc.perform(delete("http://localhost/api/pays/" + entt.getId()))
                .andExpect(status().isNoContent());
    }

    /**
	 * Test de la suppression d'un Pays. La date d'effet inférieure à la date du
	 * jour
	 *
	 * @throws Exception
	 *             Exception
	 */
    @Test
	public void shouldNotDeletePays_DateEffet() throws Exception {

		Pays entt = paysLoader.createPays("SH", "SHA", "Shambhala", LocalDate.of(2013, 1, 1),
				LocalDate.of(2013, 12, 31));

		mockMvc.perform(delete("http://localhost/api/pays/" + entt.getId()))
                .andExpect(status().isBadRequest());
    }

    /**
     * Préparation du JSON au format HAL+JSON
     *
     * @param ent une entité à transformer
     * @return JSON de l'entité, au format HAL+JSON
     * @throws Exception en cas d'erreur de parsing
     */
	private static String prepareJSON(final Pays ent) throws Exception {

        String json = JsonParser.asJsonString(ent);
        // Nothing else to do
        return json;
    }
}
