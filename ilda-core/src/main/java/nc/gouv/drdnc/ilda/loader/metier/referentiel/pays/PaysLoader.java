/**
 *
 */
package nc.gouv.drdnc.ilda.loader.metier.referentiel.pays;

import nc.gouv.drdnc.ilda.metier.referentiel.pays.Pays;
import nc.gouv.drdnc.ilda.metier.referentiel.pays.PaysRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Loader de données pour le référentiel Pays
 *
 * @author ILDA
 */
@Component
public class PaysLoader {

	private static final String AUSTRALIE = "Australie";

	private static final String A2 = "AU";

	private static final String A3 = "AUS";

	private static final String BELGIQUE = "Belgique";

	private static final String B2 = "BE";

	private static final String B3 = "BEL";

	private static final String CANADA = "Canada";

	private static final String C2 = "CA";

	private static final String C3 = "CAN";

	private static final String DANEMARK = "Danemark";

	private static final String D2 = "DK";

	private static final String D3 = "DNK";

	private List<Pays> lPays = new ArrayList<>();

	@Autowired
	PaysRepository paysRepo;

	/**
	 * Création et enregistrement d'un Pays
	 *
	 * @param code2
	 *            Code pays sur 2 car
	 * @param code3
	 *            Code pays sur 3 car
	 * @param designation
	 *            Désignation
	 * @param dateEffet
	 *            Date d'effet
	 * @param dateFinEffet
	 *            Date de fin d'effet
	 * @return l'entité créée si elle n'existe pas déjà, sinon celle de la BDD
	 */
	public Pays createPays(final String code2, final String code3, final String designation, final LocalDate dateEffet,
			final LocalDate dateFinEffet) {

		// Vérifier si le Pays existe déjà. Si oui, le réuitiliser
		Pays entt = paysRepo.findByCode2AndDateEffet(code2, dateEffet);

		if (entt == null) {
			entt = new Pays();
			entt.setCode2(code2);
			entt.setCode3(code3);
			entt.setDesignation(designation);
			entt.setDateEffet(dateEffet);
			entt.setDateFinEffet(dateFinEffet);

			entt = paysRepo.save(entt);
		}

		lPays.add(entt);

		return entt;
	}

	/**
	 * Chargement de données
	 */
	public void loadData() {
		// Australie
		createPays(A2, A3, AUSTRALIE, LocalDate.of(2013, 1, 1), LocalDate.of(2013, 12, 31));
		createPays(A2, A3, AUSTRALIE, LocalDate.of(2014, 1, 1), LocalDate.of(2014, 12, 31));
		createPays(A2, A3, AUSTRALIE, LocalDate.of(2015, 1, 1), LocalDate.of(2015, 12, 31));
		createPays(A2, A3, AUSTRALIE, LocalDate.of(2016, 1, 1), LocalDate.of(2016, 12, 31));
		createPays(A2, A3, AUSTRALIE, LocalDate.of(2035, 1, 1), null);

		// Belgique
		createPays(B2, B3, BELGIQUE, LocalDate.of(2013, 1, 1), LocalDate.of(2013, 12, 31));
		createPays(B2, B3, BELGIQUE, LocalDate.of(2014, 1, 1), LocalDate.of(2014, 12, 31));
		createPays(B2, B3, BELGIQUE, LocalDate.of(2015, 1, 1), LocalDate.of(2015, 12, 31));
		createPays(B2, B3, BELGIQUE, LocalDate.of(2016, 1, 1), LocalDate.of(2016, 12, 31));
		createPays(B2, B3, BELGIQUE, LocalDate.of(2035, 1, 1), null);

		// Canada
		createPays(C2, C3, CANADA, LocalDate.of(2013, 1, 1), LocalDate.of(2013, 12, 31));
		createPays(C2, C3, CANADA, LocalDate.of(2014, 1, 1), LocalDate.of(2014, 12, 31));
		createPays(C2, C3, CANADA, LocalDate.of(2015, 1, 1), LocalDate.of(2015, 12, 31));
		createPays(C2, C3, CANADA, LocalDate.of(2016, 1, 1), LocalDate.of(2016, 12, 31));
		createPays(C2, C3, CANADA, LocalDate.of(2035, 1, 1), null);

		// Danemark
		createPays(D2, D3, DANEMARK, LocalDate.of(2013, 1, 1), LocalDate.of(2013, 12, 31));
		createPays(D2, D3, DANEMARK, LocalDate.of(2014, 1, 1), LocalDate.of(2014, 12, 31));
		createPays(D2, D3, DANEMARK, LocalDate.of(2015, 1, 1), LocalDate.of(2015, 12, 31));
		createPays(D2, D3, DANEMARK, LocalDate.of(2016, 1, 1), LocalDate.of(2016, 12, 31));
		createPays(D2, D3, DANEMARK, LocalDate.of(2035, 1, 1), null);

	}

	/**
	 * Suppression des données
	 */
	public void removeData() {
		paysRepo.delete(lPays);
	}
}
