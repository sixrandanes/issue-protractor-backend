package nc.gouv.drdnc.ilda.metier.referentiel.pays;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import nc.gouv.drdnc.ilda.commons.historisable.BeforeCreateHistorisableValidator;
import nc.gouv.drdnc.ilda.commons.historisable.IHistorisable;

/**
 * Validation des RG sur les Pays à la création
 *
 * @author ILDA
 */
@Component("beforeCreatePaysValidator")
public class BeforeCreatePaysValidator extends BeforeCreateHistorisableValidator {

    @Autowired
    private PaysRepository paysRepo;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(final Class<?> clazz) {
        // Seule la classe Pays est acceptée
        return Pays.class.isAssignableFrom(clazz);
    }

    @Override
    protected List<IHistorisable> getSiblingsForUncityCheck(final IHistorisable entt) {
        Pays pays = (Pays) entt;
        return paysRepo.findByCode2Or3(pays.getCode2(), pays.getCode3()).stream().collect(Collectors.toList());
    }

    @Override
    protected String getUncityFieldName() {
        return "Code2/Code3";
    }

    @Override
    protected String getUncityErrorCode() {
        return "pays.code.non.unique";
    }

    @Override
    protected String getUncityDefaultErrorMsg() {
        return "Code2 et Code3 ne sont pas uniques sur la période donnée.";
    }
}
