package nl.han.oose.buizerd.projectcheck_backend.repository;

import java.util.Optional;
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
 */
@ExtendWith(MockitoExtension.class)
public class KamerRepositoryTest {

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

		Assertions.assertAll(
			() -> Assertions.assertEquals(begeleiderNaam, kamer.getBegeleider().getNaam()),
			() -> Assertions.assertEquals(kamer.getKamerCode(), kamer.getBegeleider().getDeelnemerId().getKamerCode())
		);
	}

	@Test
	void voegtToe(@Mock Kamer kamer) {
		kamerRepository.add(kamer);
		Mockito.verify(kamerDAO).create(kamer);
		Mockito.verify(begeleiderDAO).create(kamer.getBegeleider());
	}

	@Test
	void geeft_kamerAanwezig_begeleiderAanwezig(@Mock Kamer kamer, @Mock Begeleider begeleider) {
		String kamerCode = "123456";
		Optional<Kamer> kamerOpt = Optional.of(kamer);

		Mockito.when(kamerDAO.read(Kamer.class, kamerCode)).thenReturn(kamerOpt);
		Mockito.when(begeleiderDAO.read(Begeleider.class, new DeelnemerId(1L, kamerCode))).thenReturn(Optional.of(begeleider));

		Assertions.assertEquals(kamerOpt, kamerRepository.get(kamerCode));

		Mockito.verify(kamerDAO).read(Kamer.class, kamerCode);
		Mockito.verify(begeleiderDAO).read(Begeleider.class, new DeelnemerId(1L, kamerCode));
		Mockito.verify(kamer).setBegeleider(begeleider);
	}

	@Test
	void geeft_kamerAanwezig_begeleiderAfwezig(@Mock Kamer kamer) {
		String kamerCode = "123456";

		Mockito.when(kamerDAO.read(Kamer.class, kamerCode)).thenReturn(Optional.of(kamer));
		Mockito.when(begeleiderDAO.read(Begeleider.class, new DeelnemerId(1L, kamerCode))).thenReturn(Optional.empty());

		Assertions.assertEquals(Optional.empty(), kamerRepository.get(kamerCode));

		Mockito.verify(kamerDAO).read(Kamer.class, kamerCode);
		Mockito.verify(begeleiderDAO).read(Begeleider.class, new DeelnemerId(1L, kamerCode));
	}

	@Test
	void geeft_kamerAfwezig_begeleiderAanwezig(@Mock Begeleider begeleider) {
		String kamerCode = "123456";

		Mockito.when(kamerDAO.read(Kamer.class, kamerCode)).thenReturn(Optional.empty());
		Mockito.when(begeleiderDAO.read(Begeleider.class, new DeelnemerId(1L, kamerCode))).thenReturn(Optional.of(begeleider));

		Assertions.assertEquals(Optional.empty(), kamerRepository.get(kamerCode));

		Mockito.verify(kamerDAO).read(Kamer.class, kamerCode);
		Mockito.verify(begeleiderDAO).read(Begeleider.class, new DeelnemerId(1L, kamerCode));
	}

	@Test
	void geeft_kamerAfwezig_begeleiderAfwezig() {
		String kamerCode = "123456";

		Mockito.when(kamerDAO.read(Kamer.class, kamerCode)).thenReturn(Optional.empty());
		Mockito.when(begeleiderDAO.read(Begeleider.class, new DeelnemerId(1L, kamerCode))).thenReturn(Optional.empty());

		Assertions.assertEquals(Optional.empty(), kamerRepository.get(kamerCode));

		Mockito.verify(kamerDAO).read(Kamer.class, kamerCode);
		Mockito.verify(begeleiderDAO).read(Begeleider.class, new DeelnemerId(1L, kamerCode));
	}

	@Test
	void updatet(@Mock Kamer kamer) {
		kamerRepository.update(kamer);
		Mockito.verify(kamerDAO).update(kamer);
		Mockito.verify(begeleiderDAO).update(kamer.getBegeleider());
	}

	@Test
	void verwijdert() {
		String kamerCode = "123456";

		kamerRepository.remove(kamerCode);

		Mockito.verify(kamerDAO).delete(kamerCode);
		Mockito.verify(begeleiderDAO).delete(new DeelnemerId(1L, kamerCode));
	}

}
