package nl.han.oose.buizerd.projectcheck_backend.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class DeelnemerIdTest {

	private String kamerCode;
	private DeelnemerId deelnemerId;

	@BeforeEach
	void init() {
		Kamer kamer = Mockito.mock(Kamer.class);
		kamerCode = kamer.getKamerCode();
		deelnemerId = new DeelnemerId(kamer);
	}

	@Test
	void geeftJuisteDeelnemerId() {
		Assertions.assertEquals(1L, deelnemerId.getDeelnemerId());
	}

	@Test
	void geeftJuisteKamerCode() {
		Assertions.assertEquals(kamerCode, deelnemerId.getKamerCode());
	}

}
