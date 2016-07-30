package nc.gouv.drdnc.ilda.config.security;

import static nc.gouv.dtsi.trinity.cerbere.test.CerbereMockMvcRequestPostProcessors.authenticate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import nc.gouv.drdnc.ilda.config.security.user.UserIlda;
import nc.gouv.drdnc.ilda.config.security.user.UserIldaRepository;

@SpringApplicationConfiguration(classes = IldaApplication.class)
@WebIntegrationTest
@SuppressWarnings("javadoc")
public class IldaWebSecurityConfigTestIT extends AbstractTestNGSpringContextTests {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserIldaRepository userIldaRepository;

    @BeforeTest
    public void setUp() throws Exception {
        this.springTestContextPrepareTestInstance();
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).dispatchOptions(true).build();
    }

    @Test(enabled = false)
    public void shouldAccess() throws Exception {
        mockMvc.perform(
                get("http://localhost/api/unites").with(authenticate(UserServiceMock.USER_WITH_AUTHORITIES)))
                .andExpect(status().isOk());
    }

    @Test(enabled = false)
    public void shouldNotAccessWithoutAuthentication() throws Exception {
        mockMvc.perform(get("http://localhost/api/unites")).andExpect(status().isOk());
    }

    @Test(enabled = false)
    public void shouldNotAccessWithoutAuthority() throws Exception {
        mockMvc.perform(
                get("http://localhost/api/unites").with(authenticate(UserServiceMock.USER_WITHOUT_AUTHORITIES)))
                .andExpect(status().isOk());
    }

    @Test(enabled = false)
    public void shouldCreateUserAndNotAccess() throws Exception {
        mockMvc.perform(get("http://localhost/api/unites").with(authenticate(UserServiceMock.USER_TO_CREATE)))
                .andExpect(status().isOk());

        final UserIlda user = userIldaRepository.findBySub(UserServiceMock.USER_TO_CREATE);
        assertThat(user).isNotNull();
    }
}
