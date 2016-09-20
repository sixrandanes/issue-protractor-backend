package nc.gouv.drdnc.ilda.commons.historisable.simple;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import nc.gouv.drdnc.ilda.commons.utils.Constantes;
import org.springframework.data.repository.CrudRepository;
import org.springframework.validation.Errors;


/**
 * Validation des RG sur les IHistorisable à la création
 *
 * @author ILDA
 */
public abstract class BeforeSaveSimpleHistoValidator extends BeforeCreateSimpleHistoValidator {

    /**
     * Message d'erreur modification date effet
     */
    public static final String MSG_INITIAL = "Impossible de modifier la date d'effet";

    /**
     * Code de l'erreur modification date effet
     */
    public static final String ERROR_INITIAL = "initial.constraint";

    /**
     * Champ conserné par l'erreur modification date effet
     */
    public static final String FIELD_INITIAL = "Valeur initiale";

    /**
     * Message d'erreur
     */
    public static final String MSG_DATE_AFTER = "Impossible de modifier une entité après sa date d'effet.";

    /**
     * Code de l'erreur
     */
    public static final String ERROR_DATE_AFTER = "historisable.update.forbidden";

    /**
     * Champ conserné
     */
    public static final String FIELD_DATE_AFTER = "Erreur";

    @PersistenceContext
    private EntityManager entityManager;

    protected abstract CrudRepository<?, UUID> getRepository();

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(final Object target, final Errors errors) {

        // RG communes
        super.validate(target, errors);

        // RG : on ne peut pas modifier l'entité après la date d'effet
        validateModifications(errors, (ISimpleHistorisable) target);
    }

    /**
     * RG : on ne peut pas modifier l'entité après la date d'effet 
     * RG : on ne peut pas modifier un fils à la même date d'effet du père
     *
     * @param entt the object that is to be validated
     * @param errors contextual state about the validation process (never {@code null})
     */
    protected void validateModifications(final Errors errors, final ISimpleHistorisable entt) {

        LocalDate now = LocalDate.now(ZoneId.of(Constantes.GMT11));

        // Récupérer la date d'effet de BDD
        // On a besoin de détacher l'entité actuelle du cache d'Hibernate pour pouvoir récupérer les données d'origine
        // de la BDD
        entityManager.detach(entt);
        ISimpleHistorisable bddData = (ISimpleHistorisable) getRepository().findOne((UUID) entt.getId());

        if (!bddData.getDateEffet().isAfter(now)) {
            errors.rejectValue(FIELD_DATE_AFTER, ERROR_DATE_AFTER, MSG_DATE_AFTER);
        }

        if (!entt.getDateEffet().equals(bddData.getDateEffet()) && bddData.getDateEffet().equals(bddData.getParent().getDateEffet())) {
            errors.rejectValue(FIELD_INITIAL, ERROR_INITIAL, MSG_INITIAL);
        }
    }
}
