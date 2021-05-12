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
public class KamerRepositoryTest extends RepositoryTest<Kamer, String> {

	@Mock
	private DAO<Kamer, String> kamerDAO;

	@Mock
	private DAO<Begeleider, DeelnemerId> begeleiderDAO;

	private KamerRepository kamerRepository;

	@BeforeEach
	void setUp() {
		kamerRepository = new KamerRepository(kamerDAO, begeleiderDAO);
	}

	@Test
	void maaktKamerMetBegeleider() {
		String begeleiderNaam = "Joost";
		Kamer kamer = kamerRepository.maakKamer(begeleiderNaam);

		Assertions.assertNotNull(kamer);
		Assertions.assertNotNull(kamer.getBegeleider());

		Mockito.verify(begeleiderDAO).create(kamer.getBegeleider());
		Assertions.assertAll(
			() -> Assertions.assertEquals(begeleiderNaam, kamer.getBegeleider().getNaam()),
			() -> Assertions.assertEquals(kamer.getKamerCode(), kamer.getBegeleider().getDeelnemerId().getKamerCode())
		);
	}

	void voegtToe(Kamer kamer) {
		kamerRepository.add(kamer);
		Mockito.verify(kamerDAO).create(kamer);
	}

	void geeft(String s) {
		Assertions.assertTrue(kamerRepository.get(s).isEmpty());
		Mockito.verify(kamerDAO).read(Kamer.class, s);
	}

	void updatet(Kamer kamer) {
		kamerRepository.update(kamer);
		Mockito.verify(kamerDAO).update(kamer);
	}

	void verwijdert(String s) {
		kamerRepository.remove(s);
		Mockito.verify(kamerDAO).delete(s);
	}

}
