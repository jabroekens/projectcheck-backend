package nl.han.oose.buizerd.projectcheck_backend.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class KaartTest {

	private Kaart kaart;
	private static final int CODE = 1;
	private static final String TEXT = "voorbeeld tekst";

	@BeforeEach
	void setup() {
		kaart = new Kaart(CODE, TEXT);
	}

	@Test
	void geeftJuisteCodeTerug() {
		Assertions.assertEquals(CODE, kaart.getCode());
	}

	@Test
	void geeftJuisteTextTerug() {
		Assertions.assertEquals(TEXT, kaart.getText());
	}

}
