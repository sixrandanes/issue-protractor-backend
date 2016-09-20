package nc.gouv.drdnc.ilda.commons.historisable.simple;

import java.time.LocalDate;
import java.time.ZoneId;

import nc.gouv.drdnc.ilda.commons.utils.Constantes;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validation des RG sur les IHistorisable à la création
 *
 * @author ILDA
 */
public abstract class BeforeCreateSimpleHistoValidator implements Validator {

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(final Object target, final Errors errors) {

        final ISimpleHistorisable entt = (ISimpleHistorisable) target;

        if (entt.getDateEffet() != null) {

            // RG sur entité : date d'effet obligatoire

            // RG : date d'effet > date du jour
            final LocalDate now = LocalDate.now(ZoneId.of(Constantes.GMT11));
            if (!entt.getDateEffet().isAfter(now)) {
                errors.rejectValue("DateEffet", "historisable.dateEffet.future", "La date d'effet doit être strictement supérieure à la date du jour.");
            }

            // RG : date d'effet >= date effet Parent
            if (entt.getParent().getDateEffet() != null && entt.getDateEffet().isBefore(entt.getParent().getDateEffet())) {
                errors.rejectValue("DateEffet", "dateEffet.before.parent.dateEffet",
                        "La date d'effet ne peux pas être antérieure à la date d'effet du parent : " + entt.getParent().getDateEffet());
            }

            // RG : date d'effet <= date fin d'effet Parent
            if (entt.getParent().getDateFinEffet() != null && entt.getDateEffet().isAfter(entt.getParent().getDateFinEffet())) {
                errors.rejectValue("DateEffet", "dateEffet.before.parent.dateEffet",
                        "La date d'effet ne peux pas être postérieure à la date de fin d'effet du parent : " + entt.getParent().getDateFinEffet());
            }

            // RG : une seule entité avec date d'effet > date du jour
            int futures = countAfterToday(entt);
            if (futures > 0) {
                errors.rejectValue("DateEffet", "simplehisto.dateEffet.non.unique",
                        "Une seule instance autorisée avec une date d'effet supérieure à la date du jour.");
            }
        }
    }

    protected abstract int countAfterToday(ISimpleHistorisable entt);
}
