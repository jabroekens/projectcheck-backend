package nl.han.oose.buizerd.projectcheck_backend.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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

	@Test
	void geeftJuisteKamer() {
		Assertions.assertTrue(deelnemer.getKamer().isEmpty());
	}

}
