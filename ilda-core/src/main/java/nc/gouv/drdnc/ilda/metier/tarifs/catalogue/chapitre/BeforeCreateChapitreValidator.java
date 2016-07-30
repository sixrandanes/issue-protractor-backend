package nc.gouv.drdnc.ilda.metier.tarifs.catalogue.chapitre;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import nc.gouv.drdnc.ilda.commons.historisable.BeforeCreateHistorisableValidator;
import nc.gouv.drdnc.ilda.commons.historisable.IHistorisable;

/**
 * Validation des RG sur les chapitres à la création
 *
 * @author ILDA
 */
@Component("beforeCreateChapitreValidator")
public class BeforeCreateChapitreValidator extends BeforeCreateHistorisableValidator {

    @Autowired
    private ChapitreRepository chapitreRepo;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(final Class<?> clazz) {
        // Seule la classe Chapitre est acceptée
        return Chapitre.class.isAssignableFrom(clazz);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(final Object target, final Errors errors) {
        super.validate(target, errors);

        final Chapitre entt = (Chapitre) target;
        // Cas où on a une date effet
        if (entt.getDateEffet() != null) {
            // RG : date d'effet >= date effet section
            if (entt.getSection().getDateEffet() != null && entt.getDateEffet().isBefore(entt.getSection().getDateEffet())) {
                errors.rejectValue("DateEffet", "dateEffet.before.chapitre.dateEffet",
                        "La date d'effet du chapitre ne peux pas être antérieur à la date d'effet de la section");
            }
            // RG : date d'effet > date fin d'effet section
            if (entt.getSection().getDateFinEffet() != null && entt.getDateEffet().isAfter(entt.getSection().getDateFinEffet())) {
                errors.rejectValue("DateEffet", "dateEffet.before.chapitre.dateEffet",
                        "La date d'effet du chapitre ne peux pas être postérieure à la date de fin d'effet de la section");
            }

            // RG : si la date d'effet != null on doit avoir un libelleChapitre créé au même moment il faut donc
            // DesignationLibelleDefaut
            if (entt.getDesignationLibelleDefaut() == null) {
                errors.rejectValue("DesignationDefaut", "designationDefaut.missing.chapitre.libelleChapitre",
                        "Il faut une désignation par défaut pour créer un libellé par défaut");

            }
        }
        // Cas où on a une date fin effet
        if (entt.getDateFinEffet() != null) {
            // RG : date fin d'effet <= date fin d'effet section
            if (entt.getSection().getDateFinEffet() != null && entt.getDateFinEffet().isAfter(entt.getSection().getDateFinEffet())) {
                errors.rejectValue("DateFinEffet", "dateFinEffet.after.section.dateFinEffet",
                        "La date de fin d'effet du chapitre ne peux pas être postérieure à la date de fin d'effet de la section");
            }

        }

    }

    @Override
    protected List<IHistorisable> getSiblingsForUncityCheck(final IHistorisable entt) {
        Chapitre chap = (Chapitre) entt;
        return chapitreRepo.findByNumeroChapitre(chap.getNumeroChapitre()).stream().collect(Collectors.toList());
    }

    @Override
    protected String getUncityFieldName() {
        return "Numéro chapitre";
    }

    @Override
    protected String getUncityErrorCode() {
        return "chapitre.numero.non.unique";
    }

    @Override
    protected String getUncityDefaultErrorMsg() {
        return "Numéro n'est pas unique sur la période donnée.";
    }
}
