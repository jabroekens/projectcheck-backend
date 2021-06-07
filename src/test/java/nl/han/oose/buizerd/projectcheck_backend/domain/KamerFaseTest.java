package nl.han.oose.buizerd.projectcheck_backend.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class KamerFaseTest {

	@ParameterizedTest
	@EnumSource(KamerFase.class)
	void volgendeFase_geeftVolgendeFaseEnGooitNoSuchElementExceptionBijLaatsteFase(KamerFase kamerFase) {
		if (kamerFase.ordinal() == KamerFase.values().length - 1) {
			assertThrows(NoSuchElementException.class, kamerFase::getVolgendeFase);
		} else {
			var volgendeFase = kamerFase.getVolgendeFase();
			assertEquals(KamerFase.values()[kamerFase.ordinal() + 1], volgendeFase);
		}
	}

}
