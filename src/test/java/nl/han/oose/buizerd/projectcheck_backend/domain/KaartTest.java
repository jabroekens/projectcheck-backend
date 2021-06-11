package nl.han.oose.buizerd.projectcheck_backend.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
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

	@Test
	void equalsEnHashCode_isVolgensContract() {
		/*
		 * Er is nog geen ondersteuning voor de Jakarta namespace vanuit EqualsVerifier,
		 * dus moeten wij handmatig de relevante waarschuwing uitschakelen.
		 *
		 * Zie: https://0x0.st/NUzv
		 */
		EqualsVerifier.forClass(Kaart.class)
		              .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS, Warning.ALL_FIELDS_SHOULD_BE_USED)
		              .verify();
	}

}
