package nl.han.oose.buizerd.projectcheck_backend.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeelnemerTest {

	@Mock
	private DeelnemerId deelnemerId;

	private Deelnemer sut;

	@BeforeEach
	void setUp() {
		sut = new Deelnemer(deelnemerId, "Joost");
	}

	@Test
	void getDeelnemerId_geeftJuisteWaarde() {
		assertEquals(deelnemerId, sut.getDeelnemerId());
	}

	@Test
	void setEnGetNaam_zetEnGeeftJuisteWaarde() {
		var expected = "Jochem";

		sut.setNaam(expected);
		var actual = sut.getNaam();

		assertEquals(expected, actual);
	}

	@Nested
	class getKamer {

		@Mock
		private Kamer kamer;

		@Test
		void kamerAanwezig_geeftJuisteWaarde() {
			sut.setKamer(kamer);
			assertEquals(kamer, sut.getKamer());
		}

		@Test
		void kamerAfwezig_gooitIllegalStateException() {
			assertThrows(IllegalStateException.class, () -> sut.getKamer());
		}

	}

}
