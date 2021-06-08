package nl.han.oose.buizerd.projectcheck_backend.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class KaartTest {

	private static final int CODE = 1;
	private static final String TEXT = "voorbeeld tekst";

	private Kaart sut;

	@BeforeEach
	void setUp() {
		sut = new Kaart(CODE, TEXT);
	}

	@Test
	void getCode_geeftJuisteWaarde() {
		assertEquals(CODE, sut.getCode());
	}

	@Test
	void getText_geeftJuisteWaarde() {
		assertEquals(TEXT, sut.getText());
	}

}
