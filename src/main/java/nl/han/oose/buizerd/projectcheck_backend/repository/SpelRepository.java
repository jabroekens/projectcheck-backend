package nl.han.oose.buizerd.projectcheck_backend.repository;

import java.util.Optional;
import nl.han.oose.buizerd.projectcheck_backend.dao.BegeleiderDAO;
import nl.han.oose.buizerd.projectcheck_backend.dao.SpelDAO;
import nl.han.oose.buizerd.projectcheck_backend.domain.Begeleider;
import nl.han.oose.buizerd.projectcheck_backend.domain.DeelnemerId;
import nl.han.oose.buizerd.projectcheck_backend.domain.Spel;

public class SpelRepository implements Repository<Spel, Long> {

	private SpelDAO spelDAO;
	private BegeleiderDAO begeleiderDAO;

	public SpelRepository(SpelDAO spelDAO, BegeleiderDAO begeleiderDAO) {
		this.spelDAO = spelDAO;
		this.begeleiderDAO = begeleiderDAO;
	}

	/**
	 * Produces a managed room with a {@code Begeleider} named {@code begeleiderNaam}.
	 *
	 * @param begeleiderNaam The name of the {@link Begeleider} for the room.
	 * @return The newly created {@link Spel}.
	 */
	public Spel maakSpel(String begeleiderNaam) {
		Spel spel = new Spel();
		add(spel);

		Begeleider begeleider = new Begeleider(new DeelnemerId(spel.getSpelId()), begeleiderNaam);
		begeleiderDAO.create(begeleider);

		spel.setBegeleider(begeleider);
		return spel;
	}

	@Override
	public void add(Spel spel) {
		spelDAO.create(spel);
	}

	@Override
	public Optional<Spel> get(Long spelId) {
		return spelDAO.read(spelId);
	}

	@Override
	public void update(Spel spel) {
		spelDAO.update(spel);
	}

	@Override
	public void remove(Long spelId) {
		spelDAO.delete(spelId);
	}

}
