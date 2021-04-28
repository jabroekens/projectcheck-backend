package nl.han.oose.buizerd.projectcheck_backend.repository;

import java.util.Optional;
import javax.inject.Inject;
import nl.han.oose.buizerd.projectcheck_backend.dao.DAO;
import nl.han.oose.buizerd.projectcheck_backend.domain.Begeleider;
import nl.han.oose.buizerd.projectcheck_backend.domain.DeelnemerId;
import nl.han.oose.buizerd.projectcheck_backend.domain.Spel;

public class SpelRepository implements Repository<Spel, String> {

	@Inject
	DAO<Spel, String> spelDAO;
	@Inject
	DAO<Begeleider, DeelnemerId> begeleiderDAO;

	/**
	 * Produces a managed room with a {@code Begeleider} named {@code begeleiderNaam}.
	 *
	 * @param begeleiderNaam The name of the {@link Begeleider} for the room.
	 * @return The newly created {@link Spel}.
	 */
	public Spel maakSpel(String begeleiderNaam) {
		Spel spel = new Spel();
		add(spel);

		Begeleider begeleider = new Begeleider(new DeelnemerId(spel.getSpelCode()), begeleiderNaam);
		begeleiderDAO.create(begeleider);

		spel.setBegeleider(begeleider);
		return spel;
	}

	@Override
	public void add(Spel spel) {
		spelDAO.create(spel);
	}

	@Override
	public Optional<Spel> get(String spelCode) {
		return spelDAO.read(Spel.class, spelCode);
	}

	@Override
	public void update(Spel spel) {
		spelDAO.update(spel);
	}

	@Override
	public void remove(String spelCode) {
		spelDAO.delete(spelCode);
	}

}
