package nc.gouv.drdnc.ilda.metier.tarifs.catalogue.libellechapitre;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import nc.gouv.dtsi.etudes.commons.utils.Constantes;

/**
 * Validation des RG sur les libelles chapitre à la création
 *
 * @author ILDA
 */
@Component("beforeCreateLibelleChapitreValidator")
public class BeforeCreateLibelleChapitreValidator implements Validator {

    @Autowired
    private LibelleChapitreRepository libelleChapitreRepo;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(final Class<?> clazz) {
        // Seule la classe LibelleChapitre est acceptée
        return LibelleChapitre.class.isAssignableFrom(clazz);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(final Object target, final Errors errors) {
        final LibelleChapitre entt = (LibelleChapitre) target;

        if (entt.getDateEffet() != null) {
            // RG : date d'effet >= date effet chapitre
            if (entt.getChapitre().getDateEffet() != null && entt.getDateEffet().isBefore(entt.getChapitre().getDateEffet())) {
                errors.rejectValue("DateEffet", "dateEffet.before.chapitre.dateEffet",
                        "La date d'effet du libelle chapitre ne peux pas être antérieur à la date d'effet du chapitre");
            }
            // RG : date d'effet <= date fin d'effet chapitre
            if (entt.getChapitre().getDateFinEffet() != null && entt.getDateEffet().isAfter(entt.getChapitre().getDateFinEffet())) {
                errors.rejectValue("DateEffet", "dateEffet.before.chapitre.dateEffet",
                        "La date d'effet du libelle chapitre ne peux pas être posterieur à la date de fin d'effet du chapitre");
            }

            // RG : si date effet not null, alors motif effet obligatoire
            if (entt.getMotifEffet() == null || entt.getMotifEffet().length() == 0) {
                errors.rejectValue("MotifEffet", "historisable.motifEffet.notnull", "Le motif d'effet doit être renseigné.");
            }

        }
        // RG : date d'effet et designation unique, un seul libellé avec une date d'effet à null, date effet strictement
        // supérieure à la date du jour
        validateUnicite(errors, entt);

    }

    protected List<LibelleChapitre> getSiblingsForUncityCheckDateEffet(final LibelleChapitre entt) {
        return libelleChapitreRepo.findByChapitre(entt.getChapitre()).stream().collect(Collectors.toList());
    }

    protected void validateUnicite(final Errors errors, final LibelleChapitre entt) {
        LocalDate today = LocalDate.now(ZoneId.of(Constantes.GMT11));
        // On récupère les entités libelles chapitre
        List<LibelleChapitre> siblings = getSiblingsForUncityCheckDateEffet(entt);
        int countDesignation = libelleChapitreRepo.countLibelleChapitreByDesignation(entt.getDesignation());
        // RG : Une et une seule occurrence de libelleChapitre possède une Date d'effet vide OU une Date d'effet > à
        // date du jour, en d'autre mots, la date d'effet d'un libelléChapitre pour un chapitre donné est unique
        boolean rg1 = siblings.stream().anyMatch(sib -> sib.getDateEffet() == null || sib.getDateEffet().isAfter(today));

        if (rg1) {
            errors.rejectValue(getUncityFieldName(), getUncityErrorCode(), getUncityDefaultErrorMsg());
        }
        if (countDesignation > 0) {
            errors.rejectValue("Designation libellé chapitre", "libelleChapitre.designation.non.unique",
                    "Il existe déjà un libellé chapitre avec cette désignation");
        }

    }

    protected String getUncityFieldName() {
        return "Date effet libellé chapitre";
    }

    protected String getUncityErrorCode() {
        return "libelleChapitre.dateEffet.non.unique";
    }

    protected String getUncityDefaultErrorMsg() {
        return "Il existe déjà un libellé chapitre pour ce chapitre à cette date d'effet ou dans le futur";
    }
}
