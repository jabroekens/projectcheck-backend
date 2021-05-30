package nl.han.oose.buizerd.projectcheck_backend.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
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

	private Kamer kamer;

	@BeforeEach
	void setUp() {
		deelnemers = new HashSet<>();
		relevanteRollen = new HashSet<>();
		kamer = new Kamer(KamerTest.KAMER_CODE, datum, kamerFase, begeleider, deelnemers, relevanteRollen);
	}

	@Test
	void geeftJuisteKamerCode() {
		Assertions.assertEquals(KamerTest.KAMER_CODE, kamer.getKamerCode());
	}

	@Test
	void geeftJuisteDatum() {
		Assertions.assertEquals(datum, kamer.getDatum());
	}

	@Test
	void geeftJuisteKamerFase() {
		Assertions.assertEquals(kamerFase, kamer.getKamerFase());
	}

	@Test
	void zetJuisteKamerFase(@Mock KamerFase kamerFase) {
		kamer.setKamerFase(kamerFase);
		Assertions.assertEquals(kamerFase, kamer.getKamerFase());
	}

	@Test
	void geeftJuisteBegeleider() {
		Assertions.assertEquals(begeleider, kamer.getBegeleider());
	}

	@Test
	void geeftJuisteDeelnemers() {
		Set<Deelnemer> actual = kamer.getDeelnemers();

		// Eerst controleren of ze gelijk zijn, anders riskeer je een NPE bij de andere assertions
		Assertions.assertIterableEquals(actual, deelnemers);

		Assertions.assertAll(
			() -> Assertions.assertTrue(actual.contains(begeleider)),
			() -> Assertions.assertThrows(UnsupportedOperationException.class, () -> actual.add(null)),
			() -> Assertions.assertThrows(UnsupportedOperationException.class, () -> actual.remove(null))
		);
	}

	@Test
	void getDeelnemer_geeftJuisteDeelnemer(@Mock DeelnemerId deelnemerId) {
		Mockito.when(begeleider.getDeelnemerId()).thenReturn(deelnemerId);
		Assertions.assertEquals(Optional.of(begeleider), kamer.getDeelnemer(begeleider.getDeelnemerId()));
	}

	@Test
	void kamerGeeftJuisteDeelnemerId() {
		//Arrange
		Long expectedId = 2L;

		//Act
		Long actualId = kamer.genereerDeelnemerId();

		//Assert
		Assertions.assertEquals(expectedId, actualId);
	}

	@Test
	void getRelevanteRollen_geeftJuisteRollen() {
		Set<Rol> actualRelevanteRollen = kamer.getRelevanteRollen();

		Assertions.assertIterableEquals(actualRelevanteRollen, relevanteRollen);

		Assertions.assertAll(
			() -> Assertions.assertThrows(UnsupportedOperationException.class, () -> actualRelevanteRollen.add(null)),
			() -> Assertions.assertThrows(UnsupportedOperationException.class, () -> actualRelevanteRollen.remove(null))
		);
	}

	@Test
	void activeerRelevanteRollen_zetEnGeeftJuisteRelevanteRollen() {
		Set<Rol> expectedRelevanteRollen = Mockito.spy(new HashSet<>());
		kamer.activeerRelevanteRollen(expectedRelevanteRollen);
		Assertions.assertIterableEquals(expectedRelevanteRollen, kamer.getRelevanteRollen());
	}

	@Nested
	class getBegeleider {

		@Test
		void begeleiderAanwezig_geeftJuisteBegeleider() {
			Assertions.assertEquals(begeleider, kamer.getBegeleider());
		}

		@Test
		void begeleiderAfwezig_gooitIllegalStateException() {
			deelnemers.remove(begeleider);
			Assertions.assertThrows(IllegalStateException.class, () -> kamer.getBegeleider());
		}

	}

}
