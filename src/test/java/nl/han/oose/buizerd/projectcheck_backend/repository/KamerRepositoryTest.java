package nl.han.oose.buizerd.projectcheck_backend.repository;

import nl.han.oose.buizerd.projectcheck_backend.dao.DAO;
import nl.han.oose.buizerd.projectcheck_backend.domain.Begeleider;
import nl.han.oose.buizerd.projectcheck_backend.domain.DeelnemerId;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/*
 * Zie:
 *  * https://www.baeldung.com/mockito-junit-5-extension
 *  * https://stackoverflow.com/questions/12539365/when-to-use-mockito-verify
 *
 * XXX Wellicht interessant om de interface te testen?
 *  * Zie: https://stackoverflow.com/a/6724555
 */
@ExtendWith(MockitoExtension.class)
public class KamerRepositoryTest {

	@Mock
	private DAO<Kamer, String> kamerDAO;

	@Mock
	private DAO<Begeleider, DeelnemerId> begeleiderDAO;

	private KamerRepository kamerRepository;

	@BeforeEach
	void init() {
		kamerRepository = new KamerRepository(kamerDAO, begeleiderDAO);
	}

	@Test
	void maaktKamer() {
		String begeleiderNaam = "Joost";

		Begeleider begeleider = Mockito.mock(
			Begeleider.class,
			Mockito.withSettings().useConstructor(
				Mockito.mock(
					DeelnemerId.class,
					Mockito.withSettings().useConstructor(
						Mockito.mock(Kamer.class)
					)
				),
				begeleiderNaam
			)
		);

		Kamer kamer = kamerRepository.maakKamer(begeleiderNaam);

		Assertions.assertNotNull(kamer);
		Assertions.assertNotNull(kamer.getBegeleider());

		/*
		 * Omdat we niet een Begeleider kunnen mocken met dezelfde kamercode,
		 * moeten we de begeleider van de kamer zelf gebruiken.
		 */
		Mockito.verify(begeleiderDAO).create(kamer.getBegeleider());

		Assertions.assertEquals(begeleiderNaam, kamer.getBegeleider().getNaam());
		Assertions.assertEquals(kamer.getKamerCode(), kamer.getBegeleider().getDeelnemerId().getKamerCode());
	}

	void voegtKamerToe() {
		Kamer kamer = Mockito.mock(Kamer.class);
		kamerRepository.add(kamer);
		Mockito.verify(kamerDAO).create(kamer);
	}

	void geeftKamer() {
		String kamerCode = "123456";
		kamerRepository.get(kamerCode);
		Mockito.verify(kamerDAO).read(Kamer.class, kamerCode);
	}

	void updatetKamer() {
		Kamer kamer = Mockito.mock(Kamer.class);
		kamerRepository.update(kamer);
		Mockito.verify(kamerDAO).update(kamer);
	}

	void verwijdertKamer() {
		String kamerCode = "123456";
		kamerRepository.remove(kamerCode);
		Mockito.verify(kamerDAO).delete(kamerCode);
	}

}
