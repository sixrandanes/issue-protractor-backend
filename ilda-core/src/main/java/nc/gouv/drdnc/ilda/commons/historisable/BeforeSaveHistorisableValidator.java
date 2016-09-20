package nc.gouv.drdnc.ilda.commons.historisable;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import nc.gouv.drdnc.ilda.commons.utils.Constantes;
import org.springframework.validation.Errors;


/**
 * Validation des RG sur les IHistorisable à l'update
 *
 * @author ILDA
 */

public abstract class BeforeSaveHistorisableValidator extends BeforeCreateHistorisableValidator {

    @PersistenceContext
    private EntityManager entityManager;

    protected abstract HistorisableRepository<?, UUID> getRepository();

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(final Object target, final Errors errors) {

        final IHistorisable entt = (IHistorisable) target;

        // RG communes
        super.validate(target, errors);

        // RG : on ne peut pas modifier l'entité après la date d'effet (sauf date de fin d'effet)
        validateModifications(errors, entt);

        // RG : unicité du Code sur une période
        validateUniciteUpdate(errors, entt);
    }

    /**
     * RG d'unicité des entités à tout moment
     *
     * @param entt the object that is to be validated
     * @param errors contextual state about the validation process (never {@code null})
     */
    protected void validateUniciteUpdate(final Errors errors, final IHistorisable entt) {
        LocalDate today = LocalDate.now(ZoneId.of(Constantes.GMT11));
        List<IHistorisable> siblings = getSiblingsForUncityCheck(entt);
        /*
         * RG : Une et une seule occurrence de Code/Désignation possède une Date d'effet > à date du jour
         */
        boolean rg1 = siblings.stream()
                .filter(sib -> !sib.getId().equals(entt.getId()))
                .anyMatch(sib -> (entt.getDateEffet().isAfter(today))
                        && (sib.getDateEffet().isAfter(today)));
        /*
         * RG : pas de chevauchement des périodes
         */
        boolean rg2 = siblings.stream()
                .filter(sib -> !sib.getId().equals(entt.getId()))
                .anyMatch(sib -> seChevauchent(entt.getDateEffet(), sib.getDateEffet(), sib.getDateFinEffet())
                        || seChevauchent(entt.getDateFinEffet(), sib.getDateEffet(), sib.getDateFinEffet())
                        || (entt.getDateFinEffet() == null && sib.getDateFinEffet() == null)
                        || seChevauchent(sib.getDateEffet(), entt.getDateEffet(), entt.getDateFinEffet()));

        if (rg1 || rg2) {
            errors.rejectValue(getUncityFieldName(), getUncityErrorCode(), getUncityDefaultErrorMsg());
        }
    }

    /**
     * RG : on ne peut pas modifier l'entité après la date d'effet (sauf date de fin d'effet)
     *
     * @param entt the object that is to be validated
     * @param errors contextual state about the validation process (never {@code null})
     */
    protected void validateModifications(final Errors errors, final IHistorisable entt) {
        // Récupérer la date d'effet de BDD
        // On a besoin de détacher l'entité actuelle du cache d'Hibernate pour pouvoir récupérer les données d'origine
        // de la BDD
        entityManager.detach(entt);
        IHistorisable bddData = getRepository().findOne((UUID) entt.getId());

        if (bddData.getDateEffet() != null && !bddData.getDateEffet().isAfter(LocalDate.now(ZoneId.of(Constantes.GMT11)))) {

            // Déléguer la comparaison à l'entité, qui saura mieux quels champs comparer.
            if (!entt.isUpdateChangesAllowed(bddData)) {
                errors.rejectValue("Erreur", "historisable.update.forbidden", "Impossible de modifier une entité après sa date d'effet.");
            }
        } else if (bddData.getDateEffet() != null && bddData.getDateEffet().isAfter(LocalDate.now(ZoneId.of(Constantes.GMT11)))) {
            // RG : la date d'effet doit être strictement supérieure à la date du jour
            validateDateEffetSupDateJour(errors, entt);
        }
    }

    private static boolean seChevauchent(final ChronoLocalDate dateEffet, final ChronoLocalDate dateDebut, final ChronoLocalDate dateFin) {
        // La date (fin) d'effet doit être en dehors de la période [dateDebut; dateFin] (dates siblings)
        return dateEffet != null
                && (dateDebut != null && !dateEffet.isBefore(dateDebut))
                && (dateFin == null || !dateEffet.isAfter(dateFin));
    }

    @Override
    protected boolean isCreation() {
        return false;
    }
}
