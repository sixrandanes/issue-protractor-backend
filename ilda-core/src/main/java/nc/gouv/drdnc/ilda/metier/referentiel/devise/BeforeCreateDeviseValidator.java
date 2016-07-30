package nc.gouv.drdnc.ilda.metier.referentiel.devise;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import nc.gouv.drdnc.ilda.commons.historisable.BeforeCreateHistorisableValidator;
import nc.gouv.drdnc.ilda.commons.historisable.IHistorisable;
import nc.gouv.drdnc.ilda.metier.referentiel.tauxchange.TauxChange;

/**
 * Validation des RG sur les devises à la création
 *
 * @author ILDA
 */
@Component("beforeCreateDeviseValidator")
public class BeforeCreateDeviseValidator extends BeforeCreateHistorisableValidator {

    @Autowired
    private DeviseRepository deviseRepo;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(final Class<?> clazz) {
        // Seule la classe Devise est acceptée
        return Devise.class.isAssignableFrom(clazz);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(final Object target, final Errors errors) {

        // RG communes
        super.validate(target, errors);
    }

    /**
     * Vérifie si pour une devise avec une date d'effet il y a un taux d'échange effectif à la même date d'effet
     *
     * @param devise la devise pour laquelle il faut vérifier les taux de change
     * @return existe-il un taux de change associé à la devise avec la même date d'effet
     */
    public Boolean isDeviseWithValidTxChange(final Devise devise) {
        TauxChange txChange = null;
        txChange = devise.getTauxChanges().stream()
                .filter(tx -> tx != null && tx.getDateEffet() != null && tx.getDateEffet().isEqual(devise.getDateEffet()))
                .findFirst().orElse(null);
        return txChange != null;

    }

    @Override
    protected List<IHistorisable> getSiblingsForUncityCheck(final IHistorisable entt) {
        Devise dev = (Devise) entt;
        return deviseRepo.findByCodeOrDesignation(dev.getCode(), dev.getDesignation()).stream().collect(Collectors.toList());
    }

    @Override
    protected String getUncityFieldName() {
        return "Code/Désignation";
    }

    @Override
    protected String getUncityErrorCode() {
        return "devise.code.non.unique";
    }

    @Override
    protected String getUncityDefaultErrorMsg() {
        return "Code et Désignation ne sont pas uniques sur la période donnée.";
    }
}
