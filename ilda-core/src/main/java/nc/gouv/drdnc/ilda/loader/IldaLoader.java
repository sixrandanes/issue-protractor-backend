package nc.gouv.drdnc.ilda.loader;

import nc.gouv.drdnc.ilda.loader.metier.referentiel.pays.PaysLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;


/**
 * Loader de données
 *
 * @author ILDA
 */
@Component
public class IldaLoader implements ApplicationListener<ContextRefreshedEvent> {

	private boolean dataLoaded = false;



	@Autowired
	PaysLoader paysLoader;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onApplicationEvent(final ContextRefreshedEvent event) {
		if (!dataLoaded) {
			paysLoader.loadData();
			dataLoaded = true;
		}
	}

}
