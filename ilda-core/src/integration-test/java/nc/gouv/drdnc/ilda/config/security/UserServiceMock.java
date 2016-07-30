package nc.gouv.drdnc.ilda.config.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;

import nc.gouv.drdnc.ilda.config.security.user.UserIlda;
import nc.gouv.drdnc.ilda.config.security.user.UserIldaService;
import nc.gouv.dtsi.trinity.cerbere.provider.OpenIdConnectUser;
import nc.gouv.dtsi.trinity.cerbere.user.User;
import nc.gouv.dtsi.trinity.cerbere.user.UserService;

/**
 * mock pour les tests securite TODO : A supprimer par la suite et utiliser le UserService pour TI
 *
 * @author ILDA
 */
public class UserServiceMock implements UserService {

    public static final String USER_WITH_AUTHORITIES = "USER_WITH_AUTHORITIES";
    public static final String USER_WITHOUT_AUTHORITIES = "USER_WITHOUT_AUTHORITIES";
    public static final String USER_TO_CREATE = "USER_TO_CREATE";

    @Autowired
    UserIldaService userIldaService;

    /**
     * {@inheritDoc}
     */
    @Override
    public User assemble(final OpenIdConnectUser aUserInfo) throws AuthenticationException {

        UserIlda user = new UserIlda();

        switch (aUserInfo.getSub()) {
            case USER_WITH_AUTHORITIES:
                user.setRoles(Arrays.asList(Authorities.ILDA_AUTHORITY,
                        Authorities.REFERENTIEL_CONSULTATION,
                        Authorities.REFERENTIEL_CREATION,
                        Authorities.REFERENTIEL_MODIFICATION,
                        Authorities.REFERENTIEL_SUPPRESSION));
                break;
            case USER_WITHOUT_AUTHORITIES:
                user.setRoles(null);
                break;
            case USER_TO_CREATE:
                userIldaService.assemble(aUserInfo);
                break;
            default:
                break;
        }
        return user;
    }

}
