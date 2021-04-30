package nl.han.oose.buizerd.projectcheck_backend.repository;

import java.util.Optional;
import javax.inject.Inject;
import nl.han.oose.buizerd.projectcheck_backend.dao.DAO;
import nl.han.oose.buizerd.projectcheck_backend.domain.Begeleider;
import nl.han.oose.buizerd.projectcheck_backend.domain.DeelnemerId;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;

public class KamerRepository implements Repository<Kamer, String> {

	@Inject
	private DAO<Kamer, String> kamerDAO;

	@Inject
	private DAO<Begeleider, DeelnemerId> begeleiderDAO;

	/**
	 * Maakt een een kamer aan onder begeleiding van een begeleider genaamd {@code begeleiderNaam}.
	 *
	 * @param begeleiderNaam De naam van de {@link Begeleider}.
	 * @return De aangemaakte {@link Kamer}.
	 */
	public Kamer maakKamer(String begeleiderNaam) {
		Kamer kamer = new Kamer();
		add(kamer);

		Begeleider begeleider = new Begeleider(new DeelnemerId(kamer.getKamerCode()), begeleiderNaam);
		begeleiderDAO.create(begeleider);

		kamer.setBegeleider(begeleider);
		return kamer;
	}

	@Override
	public void add(Kamer kamer) {
		kamerDAO.create(kamer);
	}

	@Override
	public Optional<Kamer> get(String kamerCode) {
		return kamerDAO.read(Kamer.class, kamerCode);
	}

	@Override
	public void update(Kamer kamer) {
		kamerDAO.update(kamer);
	}

	@Override
	public void remove(String kamerCode) {
		kamerDAO.delete(kamerCode);
	}

}
