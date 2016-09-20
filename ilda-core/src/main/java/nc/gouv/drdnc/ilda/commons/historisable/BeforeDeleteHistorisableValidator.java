package nc.gouv.drdnc.ilda.commons.historisable;

import java.time.LocalDate;
import java.time.ZoneId;

import nc.gouv.drdnc.ilda.commons.utils.Constantes;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validation des RG sur les IHistorisable à la suppression
 *
 * @author ILDA
 */
@Component("beforeDeleteHistorisableValidator")
public class BeforeDeleteHistorisableValidator implements Validator {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(final Class<?> clazz) {
        // Les instances de IHistorisable sont acceptées
        return IHistorisable.class.isAssignableFrom(clazz);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(final Object target, final Errors errors) {

        final IHistorisable entt = (IHistorisable) target;

        // RG : on ne peut pas supprimer l'entité si la date d'effet est inférieure ou égale à la date du jour
        if (entt.getDateEffet() != null && !entt.getDateEffet().isAfter(LocalDate.now(ZoneId.of(Constantes.GMT11)))) {
            errors.rejectValue("Erreur", "historisable.delete.forbidden", "Impossible de supprimer une entité après sa date d'effet.");
        }

    }
}
