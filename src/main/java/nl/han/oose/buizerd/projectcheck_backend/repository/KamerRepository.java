package nl.han.oose.buizerd.projectcheck_backend.repository;

import java.util.Optional;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import nl.han.oose.buizerd.projectcheck_backend.dao.DAO;
import nl.han.oose.buizerd.projectcheck_backend.domain.Begeleider;
import nl.han.oose.buizerd.projectcheck_backend.domain.DeelnemerId;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.exception.KamerNietGevondenException;

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

		Begeleider begeleider = new Begeleider(kamer, begeleiderNaam);
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

	/**
	 * Haal een instantie van {@link Kamer} op met de code {@code kamerCode}.
	 *
	 * @param kamerCode De code van de op te halen {@link Kamer}.
	 * @return De {@link Kamer} met de code {@code kamerCode}.
	 * @throws KamerNietGevondenException Als er geen kamer met de code {@code kamerCode} is gevonden.
	 * @see nl.han.oose.buizerd.projectcheck_backend.repository.KamerRepository#get(String)
	 */
	public Kamer getKamer(@NotNull String kamerCode) throws KamerNietGevondenException {
		Optional<Kamer> kamer = get(kamerCode);
		if (kamer.isPresent()) {
			return kamer.get();
		}

		throw new KamerNietGevondenException(kamerCode);
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
