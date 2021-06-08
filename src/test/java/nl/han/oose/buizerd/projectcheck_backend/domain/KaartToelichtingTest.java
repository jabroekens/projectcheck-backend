package nl.han.oose.buizerd.projectcheck_backend.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KaartToelichtingTest {

	private static final String TOELICHTING = "testing";

	@Mock
	private Kaart kaart;

	private KaartToelichting sut;

	@BeforeEach
	void setUp() {
		sut = new KaartToelichting(kaart, TOELICHTING);
	}

	@Test
	void getKaart_geeftJuisteWaarde() {
		assertEquals(kaart, sut.getKaart());
	}

	@Test
	void setEnGetToelichting_zetEnGeeftJuisteWaarde() {
		var expected = "testing 2";

		sut.setToelichting(expected);
		var actual = sut.getToelichting();

		assertEquals(expected, actual);
	}

}
