package nl.han.oose.buizerd.projectcheck_backend.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DeelnemerTest {

	private DeelnemerId deelnemerId;
	private Deelnemer deelnemer;

	@BeforeEach
	void setUp() {
		deelnemerId = Mockito.mock(
			DeelnemerId.class,
			Mockito.withSettings().useConstructor(2L, "123456")
		);

		deelnemer = new Deelnemer(deelnemerId, "Joost");
	}

	@Test
	void geeftJuisteDeelnemerId() {
		Assertions.assertEquals(deelnemerId, deelnemer.getDeelnemerId());
	}

	@Test
	void zetEnGeeftJuisteNaam() {
		deelnemer.setNaam("Jochem");
		Assertions.assertEquals("Jochem", deelnemer.getNaam());
	}

	@Nested
	class getKamer {

		@Test
		void kamerAanwezig_geeftJuisteKamer(@Mock Kamer kamer) {
			deelnemer.setKamer(kamer);
			Assertions.assertEquals(kamer, deelnemer.getKamer());
		}

		@Test
		void kamerAfwezig_gooitIllegalStateException(@Mock Kamer kamer) {
			Assertions.assertThrows(IllegalStateException.class, () -> deelnemer.getKamer());
		}

	}

}
