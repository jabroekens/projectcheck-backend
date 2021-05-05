package nl.han.oose.buizerd.projectcheck_backend.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DeelnemerIdTest {

	private Kamer kamer;
	private DeelnemerId deelnemerId;

	@BeforeEach
	void init() {
		kamer = new Kamer();
		deelnemerId = new DeelnemerId(kamer);
	}

	@Test
	void geeftJuisteDeelnemerId() {
		Assertions.assertEquals(1L, deelnemerId.getDeelnemerId());
	}

	@Test
	void geeftJuisteKamerCode() {
		Assertions.assertEquals(kamer.getKamerCode(), deelnemerId.getKamerCode());
	}

	@Test
	void isGelijkAanGelijkwaardigeInstantie() {
		Assertions.assertEquals(deelnemerId, deelnemerId);
		Assertions.assertNotEquals(null, deelnemerId);
		Assertions.assertNotEquals(new Object(), deelnemerId);

		DeelnemerId equal = new DeelnemerId(kamer);
		Assertions.assertEquals(equal, deelnemerId);

		DeelnemerId unequalId = new DeelnemerId(2L, kamer.getKamerCode());
		Assertions.assertNotEquals(unequalId, deelnemerId);

		DeelnemerId unequalKamerCode = new DeelnemerId(deelnemerId.getDeelnemerId(), "");
		Assertions.assertNotEquals(unequalKamerCode, deelnemerId);
	}

	@Test
	void genereertDezelfdeHashCode() {
		DeelnemerId equal = new DeelnemerId(kamer);
		Assertions.assertEquals(equal.hashCode(), deelnemerId.hashCode());
	}

}
