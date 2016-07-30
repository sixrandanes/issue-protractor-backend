package nc.gouv.drdnc.ilda.metier.referentiel.incoterm;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import nc.gouv.drdnc.ilda.commons.historisable.BeforeSaveHistorisableValidator;
import nc.gouv.drdnc.ilda.commons.historisable.HistorisableRepository;
import nc.gouv.drdnc.ilda.commons.historisable.IHistorisable;

/**
 * Validation des RG sur les incoterms à la modification
 *
 * @author ILDA
 */
@Component("beforeSaveIncotermValidator")
public class BeforeSaveIncotermValidator extends BeforeSaveHistorisableValidator {

    @Autowired
    private IncotermRepository incotermRepository;

    @Override
    protected HistorisableRepository<Incoterm, UUID> getRepository() {
        return incotermRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(final Class<?> clazz) {
        // Seule la classe Incoterm est acceptée
        return Incoterm.class.isAssignableFrom(clazz);
    }

    @Override
    protected List<IHistorisable> getSiblingsForUncityCheck(final IHistorisable entt) {
        Incoterm inc = (Incoterm) entt;
        return incotermRepository.findByCodeOrDesignation(inc.getCode(), inc.getDesignation()).stream().collect(Collectors.toList());
    }

    @Override
    protected String getUncityFieldName() {
        return "Code/Désignation";
    }

    @Override
    protected String getUncityErrorCode() {
        return "incoterm.code.non.unique";
    }

    @Override
    protected String getUncityDefaultErrorMsg() {
        return "Code et Désignation ne sont pas uniques sur la période donnée.";
    }
}
