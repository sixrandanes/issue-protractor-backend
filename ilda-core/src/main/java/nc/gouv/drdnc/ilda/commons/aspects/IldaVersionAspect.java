/**
 *
 */
package nc.gouv.drdnc.ilda.commons.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import nc.gouv.dtsi.etudes.commons.aspects.VersionAspect;


/**
 * Implémentation pour Ilda du VersionAdpect
 *
 * @author ILDA
 */
@Aspect
@Component
public class IldaVersionAspect extends VersionAspect {

    @Override
    protected String getCurrentUserName() {
        // TODO à modifier une fois que l'on aura la gestion des utilisateurs
        return "DTSI";
    }

}
