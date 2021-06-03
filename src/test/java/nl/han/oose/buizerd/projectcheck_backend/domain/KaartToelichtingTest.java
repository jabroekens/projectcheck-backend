package nl.han.oose.buizerd.projectcheck_backend.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class KaartToelichtingTest {

	@Mock
	private Kaart kaart;

	private static final String TOELICHTING = "testing";

	private KaartToelichting kaartToelichting;

	@BeforeEach
	void setup() {
		kaartToelichting = new KaartToelichting(kaart, TOELICHTING);
	}

	@Test
	void zetDeJuisteToelichting() {
		String expecting = "testing 2";
		kaartToelichting.setToelichting(expecting);
		Assertions.assertEquals(expecting, kaartToelichting.getToelichting());
	}

	@Test
	void geeftJuisteToelichtingTerug() {
		Assertions.assertEquals(TOELICHTING, kaartToelichting.getToelichting());
	}

	@Test
	void geeftJuisteKaartTerug() {
		Assertions.assertEquals(kaart, kaartToelichting.getKaart());
	}

}
