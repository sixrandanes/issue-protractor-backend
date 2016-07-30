package nc.gouv.drdnc.ilda.metier.referentiel.tauxChange;

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

import java.math.BigDecimal;
import java.time.LocalDate;

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
import nc.gouv.drdnc.ilda.loader.metier.referentiel.tauxChange.TauxChangeLoader;
import nc.gouv.drdnc.ilda.metier.referentiel.devise.Devise;
import nc.gouv.drdnc.ilda.metier.referentiel.devise.DeviseRepository;
import nc.gouv.drdnc.ilda.metier.referentiel.tauxchange.TauxChange;
import nc.gouv.drdnc.ilda.metier.referentiel.tauxchange.TauxChangeRepository;
import nc.gouv.dtsi.etudes.commons.utils.JsonParser;

/**
 * Tests d'intégration de la classe Taux de Change
 *
 * @author ILDA
 */
@SpringApplicationConfiguration(classes = IldaApplication.class)
@WebIntegrationTest
public class TauxChangeTestIT extends AbstractTestNGSpringContextTests {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private TauxChangeRepository txChangeRepo;

    @Autowired
    private DeviseRepository deviseRepo;

    @Autowired
    TauxChangeLoader txChangeLoader;

    @Autowired
    private DeviseLoader deviseLoader;

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
     * @throws Exception
     */
    @BeforeMethod
    public void before() throws Exception {
        txChangeLoader.removeAllData();
        deviseLoader.removeAllData();
        deviseLoader.loadData();
        txChangeLoader.loadData();
    }

    @Test
    public void shouldCreateTxChange() throws Exception {
        Devise devise = deviseLoader.createDevise("TXT", "Devise de test taux de change", LocalDate.now().plusYears(6), LocalDate.now().plusYears(7));
        final TauxChange tx = new TauxChange();
        tx.setValeur(new BigDecimal("1234567890.12345"));
        tx.setDevise(devise);
        tx.setDateEffet(LocalDate.now().plusYears(6));
        String jsonCreate = prepareJSON(tx);
        mockMvc.perform(post("http://localhost/api/taux_changes")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(HAL_JSON))
                .andExpect(jsonPath("$.valeur", equalTo(tx.getValeur().toString())))
                .andExpect(jsonPath("$.deviseId", equalTo(tx.getDeviseId().toString())))
                // Vérifier que les dates et auteur de Création ont été renseignés
                .andExpect(jsonPath("$.auteurCre", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.dateCre", is(notNullValue())));

        TauxChange txChangeCreated = txChangeRepo.findByDeviseAndDateEffet(devise, LocalDate.now().plusYears(6));
        assertThat(txChangeCreated).isNotNull();
        assertThat(txChangeCreated.getValeur()).isEqualTo(new BigDecimal("1234567890.12345"));

    }

    /**
     * Fail car la date d'effet du taux echange < date d'effet de la devise
     * 
     * @throws Exception
     */
    @Test
    public void shouldNotCreateTxChange_DateEffetBeforeDevise() throws Exception {
        Devise devise = deviseLoader.createDevise("TXT", "Devise de test taux de change", LocalDate.now().plusYears(6), LocalDate.now().plusYears(7));
        final TauxChange tx = new TauxChange();
        tx.setValeur(new BigDecimal("1.0001"));
        tx.setDevise(devise);
        tx.setDateEffet(LocalDate.now().plusYears(4));

        String jsonCreate = prepareJSON(tx);
        mockMvc.perform(post("http://localhost/api/taux_changes")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());

    }

    /**
     * Success car la date d'effet du taux echange = date d'effet de la devise
     * 
     * @throws Exception
     */
    @Test
    public void shouldCreateTxChange_DateEffetEqualsDevise() throws Exception {
        Devise devise = deviseLoader.createDevise("TXT", "Devise de test taux de change", LocalDate.now().plusDays(1), LocalDate.now().plusYears(7));
        final TauxChange tx = new TauxChange();
        tx.setValeur(new BigDecimal("1.0001"));
        tx.setDevise(devise);
        tx.setDateEffet(LocalDate.now().plusDays(1));
        String jsonCreate = prepareJSON(tx);
        mockMvc.perform(post("http://localhost/api/taux_changes")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(HAL_JSON))
                .andExpect(jsonPath("$.valeur", equalTo(tx.getValeur().toString())))
                .andExpect(jsonPath("$.deviseId", equalTo(tx.getDeviseId().toString())))
                // Vérifier que les dates et auteur de Création ont été renseignés
                .andExpect(jsonPath("$.auteurCre", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.dateCre", is(notNullValue())));

        TauxChange txChangeCreated = txChangeRepo.findByDeviseAndDateEffet(devise, LocalDate.now().plusDays(1));
        assertThat(txChangeCreated).isNotNull();
        assertThat(txChangeCreated.getValeur()).isEqualTo(new BigDecimal("1.00010"));

    }

    /**
     * Fail car la date d'effet du taux echange > date de fin d'effet de la devise
     * 
     * @throws Exception
     */
    @Test
    public void shouldNotCreateTxChange_DateEffetAfterDevise() throws Exception {
        Devise devise = deviseLoader.createDevise("TXT", "Devise de test taux de change", LocalDate.now().plusDays(1), LocalDate.now().plusDays(4));
        final TauxChange tx = new TauxChange();
        tx.setValeur(new BigDecimal("1.0001"));
        tx.setDevise(devise);
        tx.setDateEffet(LocalDate.now().plusDays(5));
        String jsonCreate = prepareJSON(tx);
        mockMvc.perform(post("http://localhost/api/taux_changes")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());

    }

    /**
     * Fail car la date d'effet du taux echange = une autre date d'effet du taux de change
     * 
     * @throws Exception
     */
    @Test
    public void shouldNotCreateTxChange_DateEffetEqualsOther() throws Exception {
        Devise devise = deviseLoader.createDevise("TXT", "Devise de test taux de change", LocalDate.now().plusDays(1), LocalDate.now().plusYears(7));
        txChangeLoader.createTauxChange(devise, new BigDecimal("12.4455"), LocalDate.now().plusDays(1), null);

        final TauxChange tx = new TauxChange();
        tx.setValeur(new BigDecimal("1.0001"));
        tx.setDevise(devise);
        tx.setDateEffet(LocalDate.now().plusDays(1));
        String jsonCreate = prepareJSON(tx);

        mockMvc.perform(post("http://localhost/api/taux_changes")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());
    }

    /**
     * Success car la date d'effet du taux echange = une autre date d'effet du taux de change mais deux devises
     * différentes
     * 
     * @throws Exception
     */
    @Test
    public void shouldCreateTxChange_DateEffetEqualsOtherButDifferentDevise() throws Exception {
        Devise devise = deviseLoader.createDevise("TXT", "Devise de test taux de change", LocalDate.now().plusDays(1), LocalDate.now().plusYears(7));
        Devise devise1 = deviseLoader.createDevise("TX2", "Devise de test taux de change 2", LocalDate.now().plusDays(1), LocalDate.now().plusYears(7));
        txChangeLoader.createTauxChange(devise1, new BigDecimal("12.4455"), LocalDate.now().plusDays(1), null);

        final TauxChange tx = new TauxChange();
        tx.setValeur(new BigDecimal("1.0001"));
        tx.setDevise(devise);
        tx.setDateEffet(LocalDate.now().plusDays(1));
        String jsonCreate = prepareJSON(tx);

        mockMvc.perform(post("http://localhost/api/taux_changes")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(HAL_JSON))
                .andExpect(jsonPath("$.valeur", equalTo(tx.getValeur().toString())))
                .andExpect(jsonPath("$.deviseId", equalTo(tx.getDeviseId().toString())))
                // Vérifier que les dates et auteur de Création ont été renseignés
                .andExpect(jsonPath("$.auteurCre", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.dateCre", is(notNullValue())));

        TauxChange txChangeCreated = txChangeRepo.findByDeviseAndDateEffet(devise, LocalDate.now().plusDays(1));
        assertThat(txChangeCreated).isNotNull();
        assertThat(txChangeCreated.getValeur()).isEqualTo(new BigDecimal("1.00010"));
    }

    /**
     * Si on créé une devise dont la date d'effet est inférieure à la date d'aujourd'hui
     * 
     * @throws Exception
     */
    @Test
    public void shouldNotCreateTxChange_DateEffetBeforeToday() throws Exception {
        Devise devise = deviseLoader.createDevise("TXT", "Devise de test taux de change", LocalDate.now().minusDays(10), LocalDate.now().plusYears(7));

        final TauxChange tx = new TauxChange();
        tx.setValeur(new BigDecimal("1.0001"));
        tx.setDevise(devise);
        tx.setDateEffet(LocalDate.now().minusDays(5));
        String jsonCreate = prepareJSON(tx);

        mockMvc.perform(post("http://localhost/api/taux_changes")
                .contentType(HAL_JSON)
                .content(jsonCreate))
                .andExpect(status().isBadRequest());
    }

    /**
     * Si on créé une devise dont la date d'effet = à aujourd'hui
     * 
     * @throws Exception
     */
    @Test
    public void shouldNotCreateTxChange_DateEffetToday() throws Exception {
        Devise devise = deviseLoader.createDevise("TXT", "Devise de test taux de change", LocalDate.now().minusDays(10), LocalDate.now().plusYears(7));

        final TauxChange tx = new TauxChange();
        tx.setValeur(new BigDecimal("1.0001"));
        tx.setDevise(devise);
        tx.setDateEffet(LocalDate.now());
        String jsonCreate = prepareJSON(tx);

        mockMvc.perform(post("http://localhost/api/taux_changes")
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
    private static String prepareJSON(final TauxChange ent) throws Exception {

        String json = JsonParser.asJsonString(ent);
        json = json.replaceFirst("\"devise\":\\{\\}", "\"devise\":\"http://localhost/api/devises/" + ent.getDeviseId() +
                "\"");
        return json;
    }
}
