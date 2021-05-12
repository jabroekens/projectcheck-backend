package nl.han.oose.buizerd.projectcheck_backend.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DeelnemerIdTest {

	private static final Long DEELNEMER_ID = 2L;
	private static final String KAMER_CODE = "123456";

	private DeelnemerId deelnemerId;

	@BeforeEach
	void setUp() {
		deelnemerId = new DeelnemerId(DeelnemerIdTest.DEELNEMER_ID, DeelnemerIdTest.KAMER_CODE);
	}

	@Test
	void geeftJuisteDeelnemerId() {
		Assertions.assertEquals(DeelnemerIdTest.DEELNEMER_ID, deelnemerId.getId());
	}

	@Test
	void geeftJuisteKamerCode() {
		Assertions.assertEquals(DeelnemerIdTest.KAMER_CODE, deelnemerId.getKamerCode());
	}

	@Test
	void implementeertEqualsCorrect(@Mock Object object) {
		Assertions.assertAll(
			() -> Assertions.assertEquals(deelnemerId, deelnemerId),
			() -> Assertions.assertNotEquals(null, deelnemerId),
			() -> Assertions.assertNotEquals(object, deelnemerId),
			() -> {
				DeelnemerId equal = new DeelnemerId(deelnemerId.getId(), deelnemerId.getKamerCode());
				Assertions.assertEquals(equal, deelnemerId);
			},
			() -> {
				DeelnemerId unequalId = new DeelnemerId(deelnemerId.getId() + 1L, deelnemerId.getKamerCode());
				Assertions.assertNotEquals(unequalId, deelnemerId);
			},
			() -> {
				DeelnemerId unequalKamerCode = new DeelnemerId(deelnemerId.getId(), deelnemerId.getKamerCode() + " ");
				Assertions.assertNotEquals(unequalKamerCode, deelnemerId);
			}
		);
	}

	@Test
	void implementeertHashCodeCorrect() {
		DeelnemerId equal = new DeelnemerId(deelnemerId.getId(), deelnemerId.getKamerCode());
		Assertions.assertEquals(equal.hashCode(), deelnemerId.hashCode());
	}

}
