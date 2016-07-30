package nc.gouv.drdnc.ilda.loader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import nc.gouv.drdnc.ilda.loader.metier.referentiel.devise.DeviseLoader;
import nc.gouv.drdnc.ilda.loader.metier.referentiel.incoterm.IncotermLoader;
import nc.gouv.drdnc.ilda.loader.metier.referentiel.pays.PaysLoader;
import nc.gouv.drdnc.ilda.loader.metier.referentiel.tauxChange.TauxChangeLoader;
import nc.gouv.drdnc.ilda.loader.metier.referentiel.unite.UniteLoader;
import nc.gouv.drdnc.ilda.loader.metier.tarifs.catalogue.chapitre.ChapitreLoader;

/**
 * Loader de données
 *
 * @author ILDA
 */
@Component
@Profile({ "local", "jenkins" })
public class IldaLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean dataLoaded = false;

    @Autowired
    UniteLoader unitesLoader;
    @Autowired
    IncotermLoader incotermsLoader;

    @Autowired
    DeviseLoader deviseLoader;

    @Autowired
    TauxChangeLoader txChangeLoader;

    @Autowired
    PaysLoader paysLoader;

    @Autowired
    ChapitreLoader chapitreLoader;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (!dataLoaded) {
            unitesLoader.loadData();
            incotermsLoader.loadData();
            deviseLoader.loadData();
            txChangeLoader.loadData();
            paysLoader.loadData();
            chapitreLoader.loadData();

            dataLoaded = true;
        }
    }

}
