package nl.han.oose.buizerd.projectcheck_backend.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KamerTest {

	private static final String KAMER_CODE = "123456";

	@Mock
	private Begeleider begeleider;

	private Kamer sut;

	@BeforeEach
	void setUp() {
		sut = new Kamer(KAMER_CODE, begeleider);
	}

	@Test
	void new_voegtBegeleiderToeAanDeelnemers() {
		assertTrue(sut.getDeelnemers().contains(begeleider));
	}

	@Test
	void getKamerCode_geeftJuisteWaarde() {
		assertEquals(KAMER_CODE, sut.getKamerCode());
	}

	@Test
	void getDatum_isInHetVerledenOfHeden() {
		assertTrue(sut.getDatum().compareTo(LocalDateTime.now()) <= 0);
	}

	@Test
	void setEnGetKamerFase_zetEnGeeftJuisteWaarde(@Mock KamerFase kamerFase) {
		sut.setKamerFase(kamerFase);
		assertEquals(kamerFase, sut.getKamerFase());
	}

	@Test
	void getDeelnemers_geeftJuisteWaardenEnVerbiedtToevoegenEnVerwijderen() {
		var expected = Set.<Deelnemer>of(begeleider);

		var actual = sut.getDeelnemers();

		// Eerst controleren of ze gelijk zijn, anders riskeer je een NPE bij de andere assertions
		assertIterableEquals(expected, actual);
		assertAll(
			() -> assertThrows(UnsupportedOperationException.class, () -> actual.add(null)),
			() -> assertThrows(UnsupportedOperationException.class, () -> actual.remove(null))
		);
	}

	@Test
	void getDeelnemer_geeftJuisteWaarde(@Mock DeelnemerId deelnemerId) {
		var expected = Optional.of(begeleider);
		when(begeleider.getDeelnemerId()).thenReturn(deelnemerId);

		var actual = sut.getDeelnemer(begeleider.getDeelnemerId());

		assertEquals(expected, actual);
	}

	@Test
	void genereerDeelnemerId_geeftJuisteWaarde(@Mock Deelnemer deelnemer) {
		for (long expected = 2L; expected < 4L; expected++) {
			var actual = sut.genereerDeelnemerId();
			sut.addDeelnemer(deelnemer);
			assertEquals(expected, actual);
		}
	}

	@Test
	void getRelevanteRollen_geeftJuisteWaarden() {
		var expected = Set.<Rol>of();

		var actual = sut.getRelevanteRollen();

		assertIterableEquals(expected, actual);
		assertAll(
			() -> assertThrows(UnsupportedOperationException.class, () -> actual.add(null)),
			() -> assertThrows(UnsupportedOperationException.class, () -> actual.remove(null))
		);
	}

	@Test
	void setRelevanteRollen_zetEnGeeftJuisteWaarden() {
		var expected = spy(new HashSet<Rol>());

		sut.setRelevanteRollen(expected);
		var actual = sut.getRelevanteRollen();

		assertIterableEquals(expected, actual);
	}

	@Test
	void begeleiderAanwezig_geeftJuisteWaarde() {
		assertEquals(begeleider, sut.getBegeleider());
	}

}
