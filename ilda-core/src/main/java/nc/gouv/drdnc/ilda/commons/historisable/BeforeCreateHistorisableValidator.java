package nc.gouv.drdnc.ilda.commons.historisable;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import nc.gouv.dtsi.etudes.commons.utils.Constantes;

/**
 * Validation des RG sur les IHistorisable à la création
 *
 * @author ILDA
 */
public abstract class BeforeCreateHistorisableValidator implements Validator {

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(final Object target, final Errors errors) {

        final IHistorisable entt = (IHistorisable) target;

        // RG : la date de fin d'effet doit être strictement supérieure à la date d'effet
        validateDateFinEffetSupDateEffet(errors, entt);

        // RG : si la date de fin d'effet est renseignée, la date d'effet est obligatoire
        validateDateEffetObligatoire(errors, entt);

        // RGs applicables uniquement en création
        if (isCreation()) {

            // RG : la date d'effet doit être strictement supérieure à la date du jour
            validateDateEffetSupDateJour(errors, entt);

            // RG : unicité du Code sur une période
            validateUnicite(errors, entt);
        }
    }

    /**
     * RG d'unicité des entités à tout moment
     *
     * @param entt the object that is to be validated
     * @param errors contextual state about the validation process (never {@code null})
     */
    protected void validateUnicite(final Errors errors, final IHistorisable entt) {
        LocalDate today = LocalDate.now(ZoneId.of(Constantes.GMT11));
        // On récupère les entités historiques (même code, désignation, etc.)
        List<IHistorisable> siblings = getSiblingsForUncityCheck(entt);
        // RG : Une et une seule occurrence de Code/Désignation possède une Date d'effet vide OU une Date d'effet >
        // à date du jour OU une Date de fin d'effet = vide
        boolean rg1 = siblings.stream().anyMatch(sib -> sib.getDateEffet() == null || sib.getDateEffet().isAfter(today) || sib.getDateFinEffet() == null);
        // RG : Date d'effet > à toutes les Date de fin d'effet des autres occurrences lorsque Date d'effet <> vide
        boolean rg2 = siblings.stream()
                .anyMatch(sib -> entt.getDateEffet() != null && (sib.getDateFinEffet() == null || !sib.getDateFinEffet().isBefore(entt.getDateEffet())));

        if (rg1 || rg2) {
            errors.rejectValue(getUncityFieldName(), getUncityErrorCode(), getUncityDefaultErrorMsg());
        }
    }

    /**
     * RG : la date de fin d'effet doit être strictement supérieure à la date d'effet
     *
     * @param entt the object that is to be validated
     * @param errors contextual state about the validation process (never {@code null})
     */
    protected void validateDateFinEffetSupDateEffet(final Errors errors, final IHistorisable entt) {
        if (entt.getDateEffet() != null
                && entt.getDateFinEffet() != null
                && !entt.getDateFinEffet().isAfter(entt.getDateEffet())) {
            errors.rejectValue("DateFinEffet", "historisable.dateEffet.sup.dateFinEffet",
                    "La date de fin d'effet doit être strictement supérieure à la date d'effet.");
        }
    }

    /**
     * RG : si la date de fin d'effet est renseignée, la date d'effet est obligatoire
     *
     * @param entt the object that is to be validated
     * @param errors contextual state about the validation process (never {@code null})
     */
    protected void validateDateEffetObligatoire(final Errors errors, final IHistorisable entt) {
        if (entt.getDateEffet() == null && entt.getDateFinEffet() != null) {
            errors.rejectValue("DateEffet", "historisable.dateEffet.mandatory", "La date d'effet doit être renseignée.");
        }
    }

    /**
     * RG : la date d'effet doit être strictement supérieure à la date du jour
     *
     * @param entt the object that is to be validated
     * @param errors contextual state about the validation process (never {@code null})
     */
    protected void validateDateEffetSupDateJour(final Errors errors, final IHistorisable entt) {
        final LocalDate now = LocalDate.now(ZoneId.of(Constantes.GMT11));
        if (entt.getDateEffet() != null && !entt.getDateEffet().isAfter(now)) {
            errors.rejectValue("DateEffet", "historisable.dateEffet.future", "La date d'effet doit être strictement supérieure à la date du jour.");
        }
    }

    protected boolean isCreation() {
        return true;
    }

    protected abstract List<IHistorisable> getSiblingsForUncityCheck(IHistorisable entt);

    protected abstract String getUncityFieldName();

    protected abstract String getUncityErrorCode();

    protected abstract String getUncityDefaultErrorMsg();
}
