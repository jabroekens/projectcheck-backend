package nl.han.oose.buizerd.projectcheck_backend.repository;

import jakarta.inject.Inject;
import java.util.Optional;
import nl.han.oose.buizerd.projectcheck_backend.dao.DAO;
import nl.han.oose.buizerd.projectcheck_backend.domain.Begeleider;
import nl.han.oose.buizerd.projectcheck_backend.domain.DeelnemerId;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;

/**
 * Een KamerRepository voor het beheren van {@link Kamer}.
 *
 * @see nl.han.oose.buizerd.projectcheck_backend.repository.Repository
 */
public class KamerRepository implements Repository<Kamer, String> {

	private final DAO<Kamer, String> kamerDAO;
	private final DAO<Begeleider, DeelnemerId> begeleiderDAO;

	/**
	 * Construeert een {@link KamerRepository}.
	 *
	 * @param kamerDAO De {@link DAO} verantwoordelijk voor {@link Kamer}.
	 * @param begeleiderDAO De {@link DAO} verantwoordelijk voor {@link Begeleider}.
	 */
	@Inject
	public KamerRepository(DAO<Kamer, String> kamerDAO, DAO<Begeleider, DeelnemerId> begeleiderDAO) {
		this.kamerDAO = kamerDAO;
		this.begeleiderDAO = begeleiderDAO;
	}

	/**
	 * Maakt een een kamer aan onder begeleiding van een begeleider genaamd {@code begeleiderNaam}.
	 *
	 * @param begeleiderNaam De naam van de {@link Begeleider}.
	 * @return De aangemaakte {@link Kamer}.
	 */
	public Kamer maakKamer(String begeleiderNaam) {
		String kamerCode = Kamer.genereerCode();

		Begeleider begeleider = new Begeleider(new DeelnemerId(1L, kamerCode), begeleiderNaam);

		Kamer kamer = new Kamer(kamerCode, begeleider);
		add(kamer);

		return kamer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(Kamer kamer) {
		kamerDAO.create(kamer);
		begeleiderDAO.create(kamer.getBegeleider());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Kamer> get(String kamerCode) {
		Optional<Kamer> kamer = kamerDAO.read(Kamer.class, kamerCode);
		Optional<Begeleider> begeleider = begeleiderDAO.read(Begeleider.class, new DeelnemerId(1L, kamerCode));

		if (kamer.isPresent() && begeleider.isPresent()) {
			kamer.get().setBegeleider(begeleider.get());
			return kamer;
		} else {
			return Optional.empty();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(Kamer kamer) {
		kamerDAO.update(kamer);
		begeleiderDAO.update(kamer.getBegeleider());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(String kamerCode) {
		kamerDAO.delete(kamerCode);
		begeleiderDAO.delete(new DeelnemerId(1L, kamerCode));
	}

}
