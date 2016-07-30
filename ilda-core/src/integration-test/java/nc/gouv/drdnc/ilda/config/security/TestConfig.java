package nc.gouv.drdnc.ilda.config.security;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import nc.gouv.dtsi.trinity.cerbere.provider.OAuth2Provider;
import nc.gouv.dtsi.trinity.cerbere.provider.OAuth2Providers;
import nc.gouv.dtsi.trinity.cerbere.test.MockOpenIdConnectUser;
import nc.gouv.dtsi.trinity.cerbere.user.UserService;

@Configuration
@Profile({ "local", "jenkins" })
public class TestConfig {

    @Bean
    @Primary
    public UserService userService() {
        return new UserServiceMock();
    }

    @Autowired
    private OAuth2Providers oauth2Providers;

    @PostConstruct
    public void addMockOAuth2Provider() {
        final OAuth2Provider mockProvider = new OAuth2Provider();
        mockProvider.setId("mock");
        mockProvider.setUserInfoClass(MockOpenIdConnectUser.class);
        mockProvider.setUserInfoUrl("https://mock/user");
        mockProvider.setAccessTokenUrl("https://mock/token");
        mockProvider.setAuthorizationUrl("https://mock/authorize");
        oauth2Providers.addProvider(mockProvider);
    }
}
