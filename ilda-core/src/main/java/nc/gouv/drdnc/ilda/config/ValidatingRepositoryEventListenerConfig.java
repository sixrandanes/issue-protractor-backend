package nc.gouv.drdnc.ilda.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.validation.Validator;

/**
 * Classe qui permet de rattacher tous les Validator au validatingRepositoryEventListener
 *
 * Created by ILDA
 */
@Configuration
public class ValidatingRepositoryEventListenerConfig implements InitializingBean {

    private static final List<String> EVENTS;

    // Liste des "hooks" possibles sur les évènements
    static {
        final List<String> events = new ArrayList<>();
        events.add("beforeCreate");
        events.add("afterCreate");
        events.add("beforeSave");
        events.add("afterSave");
        events.add("beforeLinkSave");
        events.add("afterLinkSave");
        events.add("beforeDelete");
        events.add("afterDelete");
        EVENTS = Collections.unmodifiableList(events);
    }

    @Autowired
    private ListableBeanFactory beanFactory;

    @Autowired
    private ValidatingRepositoryEventListener validatingRepositoryEventListener;

    /**
     * Methode qui permet de recuperer tous les objets de type validators dont le nom commencent par un des evenements ci-dessus. Chacun d'entre eux est ensuite
     * abonné au listener validatingRepositoryEventListener.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        final Map<String, Validator> validators = beanFactory.getBeansOfType(Validator.class);

        // Pour chaque validator, trouver le "hook" qui le caractérise
        // puis l'ajouter à l'EventListener correspondant.
        for (final Map.Entry<String, Validator> entry : validators.entrySet()) {
            EVENTS.stream()
                    .filter(event -> entry.getKey().startsWith(event))
                    .findFirst()
                    .ifPresent(event -> validatingRepositoryEventListener.addValidator(event, entry.getValue()));
        }

        // Ajouter la validation JSR-303 avant le create et save
        validatingRepositoryEventListener.addValidator("beforeCreate", validators.get("mvcValidator"));
        validatingRepositoryEventListener.addValidator("beforeSave", validators.get("mvcValidator"));
    }
}
