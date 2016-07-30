package nc.gouv.drdnc.ilda.commons.historisable.motif;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validation des RG sur les IHistorisableAvecMotif à la création
 *
 * @author ILDA
 */
@Component("beforeCreateHistoMotifValidator")
public class BeforeCreateHistoMotifValidator implements Validator {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(final Class<?> clazz) {
        // Les instances de IHistorisableAvecMotif sont acceptées
        return IHistorisableAvecMotif.class.isAssignableFrom(clazz);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(final Object target, final Errors errors) {

        final IHistorisableAvecMotif entt = (IHistorisableAvecMotif) target;

        // RG : si date effet not null, alors motif effet obligatoire
        if (entt.getDateEffet() != null && (entt.getMotifEffet() == null || entt.getMotifEffet().length() == 0)) {
            errors.rejectValue("MotifEffet", "historisable.motifEffet.notnull", "Le motif d'effet doit être renseigné.");
        }

        // RG : si date fin effet not null, alors motif fin effet obligatoire
        if (entt.getDateFinEffet() != null && (entt.getMotifFinEffet() == null || entt.getMotifFinEffet().length() == 0)) {
            errors.rejectValue("MotifFinEffet", "historisable.motifFinEffet.notnull", "Le motif de fin d'effet doit être renseigné.");
        }

    }
}
