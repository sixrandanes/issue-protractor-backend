package nc.gouv.drdnc.ilda.metier.referentiel.devise;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import nc.gouv.drdnc.ilda.commons.historisable.BeforeSaveHistorisableValidator;
import nc.gouv.drdnc.ilda.commons.historisable.HistorisableRepository;
import nc.gouv.drdnc.ilda.commons.historisable.IHistorisable;

/**
 * Validation des RG sur les Devises à la modification
 *
 * @author ILDA
 */
@Component("beforeSaveDeviseValidator")
public class BeforeSaveDeviseValidator extends BeforeSaveHistorisableValidator {

    @Autowired
    private DeviseRepository deviseRepo;

    @Override
    protected HistorisableRepository<Devise, UUID> getRepository() {
        return deviseRepo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(final Class<?> clazz) {
        // Seule la classe Devise est acceptée
        return Devise.class.isAssignableFrom(clazz);
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
