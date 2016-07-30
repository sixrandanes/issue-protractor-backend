package nc.gouv.drdnc.ilda.metier.referentiel.unite;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import nc.gouv.drdnc.ilda.commons.historisable.BeforeCreateHistorisableValidator;
import nc.gouv.drdnc.ilda.commons.historisable.IHistorisable;

/**
 * Validation des RG sur les Unités à la création
 *
 * @author ILDA
 */
@Component("beforeCreateUniteValidator")
public class BeforeCreateUniteValidator extends BeforeCreateHistorisableValidator {

    @Autowired
    private UniteRepository uniteRepo;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(final Class<?> clazz) {
        // Seule la classe Unite est acceptée
        return Unite.class.isAssignableFrom(clazz);
    }

    @Override
    protected List<IHistorisable> getSiblingsForUncityCheck(final IHistorisable entt) {
        Unite unit = (Unite) entt;
        return uniteRepo.findByCodeOrDesignation(unit.getCode(), unit.getDesignation()).stream().collect(Collectors.toList());
    }

    @Override
    protected String getUncityFieldName() {
        return "Code/Désignation";
    }

    @Override
    protected String getUncityErrorCode() {
        return "unite.code.non.unique";
    }

    @Override
    protected String getUncityDefaultErrorMsg() {
        return "Code et Désignation ne sont pas uniques sur la période donnée.";
    }
}
