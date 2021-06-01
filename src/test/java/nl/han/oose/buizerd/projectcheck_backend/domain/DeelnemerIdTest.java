package nl.han.oose.buizerd.projectcheck_backend.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeelnemerIdTest {

	private static final Long DEELNEMER_ID = 2L;
	private static final String KAMER_CODE = "123456";

	private DeelnemerId sut;

	@BeforeEach
	void setUp() {
		sut = new DeelnemerId(DeelnemerIdTest.DEELNEMER_ID, DeelnemerIdTest.KAMER_CODE);
	}

	@Test
	void getId_geeftJuisteWaarde() {
		assertEquals(DeelnemerIdTest.DEELNEMER_ID, sut.getId());
	}

	@Test
	void getKamerCode_geeftJuisteWaarde() {
		assertEquals(DeelnemerIdTest.KAMER_CODE, sut.getKamerCode());
	}

	@Nested
	class equals {

		@Test
		void gelijkBijDezelfdeReferentie() {
			assertEquals(sut, sut);
		}

		@Test
		void ongelijkBijNullWaarde() {
			assertNotEquals(null, sut);
		}

		@Test
		void ongelijkBijAndereKlasse(@Mock Object object) {
			assertNotEquals(object, sut);
		}

		@Test
		void gelijkBijGelijkeWaarden() {
			DeelnemerId equal = new DeelnemerId(sut.getId(), sut.getKamerCode());
			assertEquals(equal, sut);
		}

		@Test
		void ongelijkBijOngelijkeId() {
			DeelnemerId unequal = new DeelnemerId(sut.getId() + 1L, sut.getKamerCode());
			assertNotEquals(unequal, sut);
		}

		@Test
		void ongelijkBijOngelijkeKamerCode() {
			DeelnemerId unequal = new DeelnemerId(sut.getId(), sut.getKamerCode() + "1");
			assertNotEquals(unequal, sut);
		}

	}

	@Nested
	class hashCode {

		@TestFactory
		Stream<DynamicTest> gelijkBijMeerdereAanroepen() {
			return Stream.of(
				dynamicTest("1", () -> assertEquals(sut.hashCode(), sut.hashCode())),
				dynamicTest("2", () -> assertEquals(sut.hashCode(), sut.hashCode())),
				dynamicTest("3", () -> assertEquals(sut.hashCode(), sut.hashCode()))
			);
		}

		@Test
		void gelijkBijGelijkeDeelnemerIds() {
			DeelnemerId equal = new DeelnemerId(sut.getId(), sut.getKamerCode());
			assertEquals(equal.hashCode(), sut.hashCode());
		}

	}

}
