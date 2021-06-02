package nl.han.oose.buizerd.projectcheck_backend.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

	@Test
	void equalsEnHashCode_isVolgensContract() {
		/*
		 * Er is nog geen ondersteuning voor de Jakarta namespace vanuit EqualsVerifier,
		 * dus moeten wij handmatig de relevante waarschuwing uitschakelen.
		 *
		 * Zie: https://0x0.st/NUzv
		 */
		EqualsVerifier.forClass(DeelnemerId.class)
		              .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
		              .verify();
	}

}
