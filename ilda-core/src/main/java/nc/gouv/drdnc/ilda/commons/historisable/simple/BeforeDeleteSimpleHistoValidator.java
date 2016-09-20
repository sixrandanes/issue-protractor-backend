package nc.gouv.drdnc.ilda.commons.historisable.simple;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validation des RG sur les simple history lors de la suppression
 *
 * @author ILDA
 */
public abstract class BeforeDeleteSimpleHistoValidator implements Validator {

    /**
     * Message d'erreur par défaut
     */
    public static final String DEFAULT_MSG = "Impossible de supprimer l'entité";

    /**
     * Code de l'erreur
     */
    public static final String ERROR_CODE = "initial.constraint";

    /**
     * Champ conserné par l'erreur
     */
    public static final String FIELD = "Valeur initiale";

    @Override
    public void validate(final Object target, final Errors errors) {
        final ISimpleHistorisable entt = (ISimpleHistorisable) target;

        // RG : Au moins un enfant à la date d'effet du parent
        if (entt.getDateEffet().equals(entt.getParent().getDateEffet())) {
            errors.rejectValue(FIELD, ERROR_CODE,
                    DEFAULT_MSG);
        }
    }
}
