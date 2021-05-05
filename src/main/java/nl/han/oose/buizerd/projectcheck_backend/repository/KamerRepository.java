package nl.han.oose.buizerd.projectcheck_backend.repository;

import java.util.Optional;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
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
	public KamerRepository(@NotNull DAO<Kamer, String> kamerDAO, @NotNull DAO<Begeleider, DeelnemerId> begeleiderDAO) {
		this.kamerDAO = kamerDAO;
		this.begeleiderDAO = begeleiderDAO;
	}

	/**
	 * Maakt een een kamer aan onder begeleiding van een begeleider genaamd {@code begeleiderNaam}.
	 *
	 * @param begeleiderNaam De naam van de {@link Begeleider}.
	 * @return De aangemaakte {@link Kamer}.
	 */
	public Kamer maakKamer(@NotNull String begeleiderNaam) {
		Kamer kamer = new Kamer();
		add(kamer);

		// De begeleider heeft altijd deelnemerId `1`.
		Begeleider begeleider = new Begeleider(new DeelnemerId(1L, kamer.getKamerCode()), begeleiderNaam);
		begeleiderDAO.create(begeleider);

		kamer.setBegeleider(begeleider);
		return kamer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(Kamer kamer) {
		kamerDAO.create(kamer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Kamer> get(String kamerCode) {
		return kamerDAO.read(Kamer.class, kamerCode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(Kamer kamer) {
		kamerDAO.update(kamer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(String kamerCode) {
		kamerDAO.delete(kamerCode);
	}

}
