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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KamerTest {

	private static final String KAMER_CODE = "123456";

	@Mock
	private LocalDateTime datum;

	@Mock
	private KamerFase kamerFase;

	/*
	 * Er wordt gebruikt gemaakt van @Spy voor Collection-objecten,
	 * omdat deze niets 'bijhouden' als deze gemockt zijn.
	 */
	@Spy
	private Set<Rol> relevanteRollen;

	@Spy
	private Set<Deelnemer> deelnemers;

	@Mock
	private Begeleider begeleider;

	private Kamer sut;

	@BeforeEach
	void setUp() {
		deelnemers = new HashSet<>();
		relevanteRollen = new HashSet<>();
		sut = new Kamer(KamerTest.KAMER_CODE, datum, kamerFase, begeleider, deelnemers, relevanteRollen);
	}

	@Test
	void new_voegtBegeleiderToeAanDeelnemers() {
		assertTrue(deelnemers.contains(begeleider));
	}

	@Test
	void getKamerCode_geeftJuisteWaarde() {
		assertEquals(KamerTest.KAMER_CODE, sut.getKamerCode());
	}

	@Test
	void getDatum_geeftJuisteWaarde() {
		assertEquals(datum, sut.getDatum());
	}

	@Test
	void setEnGetKamerFase_zetEnGeeftJuisteWaarde(@Mock KamerFase kamerFase) {
		sut.setKamerFase(kamerFase);
		assertEquals(kamerFase, sut.getKamerFase());
	}

	@Test
	void getBegeleider_geeftJuisteWaarde() {
		assertEquals(begeleider, sut.getBegeleider());
	}

	@Test
	void getDeelnemers_geeftJuisteWaardenEnVerbiedtToevoegenEnVerwijderen() {
		Set<Deelnemer> actual = sut.getDeelnemers();

		// Eerst controleren of ze gelijk zijn, anders riskeer je een NPE bij de andere assertions
		assertIterableEquals(actual, deelnemers);

		assertAll(
			() -> assertTrue(actual.contains(begeleider)),
			() -> assertThrows(UnsupportedOperationException.class, () -> actual.add(null)),
			() -> assertThrows(UnsupportedOperationException.class, () -> actual.remove(null))
		);
	}

	@Test
	void getDeelnemer_geeftJuisteWaarde(@Mock DeelnemerId deelnemerId) {
		when(begeleider.getDeelnemerId()).thenReturn(deelnemerId);
		assertEquals(Optional.of(begeleider), sut.getDeelnemer(begeleider.getDeelnemerId()));
	}

	@Test
	void genereerDeelnemerId_geeftJuisteWaarde(@Mock Deelnemer deelnemer) {
		//Arrange
		for (long expected = 2L; expected < 4L; expected++) {
			//Act
			Long actual = sut.genereerDeelnemerId();
			sut.voegDeelnemerToe(deelnemer);

			//Assert
			assertEquals(expected, actual);
		}
	}

	@Test
	void getRelevanteRollen_geeftJuisteWaarden() {
		Set<Rol> actualRelevanteRollen = sut.getRelevanteRollen();

		assertIterableEquals(actualRelevanteRollen, relevanteRollen);

		assertAll(
			() -> assertThrows(UnsupportedOperationException.class, () -> actualRelevanteRollen.add(null)),
			() -> assertThrows(UnsupportedOperationException.class, () -> actualRelevanteRollen.remove(null))
		);
	}

	@Test
	void activeerRelevanteRollen_zetEnGeeftJuisteWaarden() {
		Set<Rol> expected = spy(new HashSet<>());
		sut.activeerRelevanteRollen(expected);
		assertIterableEquals(expected, sut.getRelevanteRollen());
	}

	@Nested
	class getBegeleider {

		@Test
		void begeleiderAanwezig_geeftJuisteWaarde() {
			assertEquals(begeleider, sut.getBegeleider());
		}

		@Test
		void begeleiderAfwezig_gooitIllegalStateException() {
			deelnemers.remove(begeleider);
			assertThrows(IllegalStateException.class, () -> sut.getBegeleider());
		}

	}

}
