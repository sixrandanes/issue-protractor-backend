package nc.gouv.drdnc.ilda.metier.referentiel.tauxchange;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validation des RG sur les taux de change à la création
 *
 * @author ILDA
 */
@Component("beforeCreateTauxChangeValidator")
public class BeforeCreateTauxChangeValidator implements Validator {

    @Autowired
    private TauxChangeRepository txChangeRepo;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(final Class<?> clazz) {
        // Seule la classe Devise est acceptée
        return TauxChange.class.isAssignableFrom(clazz);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(final Object target, final Errors errors) {
        final TauxChange entt = (TauxChange) target;

        if (entt.getDateEffet() != null) {
            // RG : date d'effet >= date effet devise
            if (entt.getDevise().getDateEffet() != null && entt.getDateEffet().isBefore(entt.getDevise().getDateEffet())) {
                errors.rejectValue("DateEffet", "dateEffet.before.devise.dateEffet",
                        "La date d'effet du taux de change ne peux pas être antérieur à la date d'effet de la devise");
            }
            // RG : date d'effet <= date fin d'effet devise
            if (entt.getDevise().getDateFinEffet() != null && entt.getDateEffet().isAfter(entt.getDevise().getDateFinEffet())) {
                errors.rejectValue("DateEffet", "dateEffet.before.devise.dateEffet",
                        "La date d'effet du taux de change ne peux pas être antérieur à la date d'effet de la devise");
            }
            // RG : date d'effet unique pour une même devise
            int countTxChange = txChangeRepo.countUniqueDateEffet(entt.getDateEffet(), entt.getDevise());
            if (countTxChange > 0) {
                errors.rejectValue("dateEffet", "txChange.dateEffet.non.unique", "La date d'effet n'est pas unique pour la devise donnée.");
            }
        }

    }
}
