package nl.han.oose.buizerd.projectcheck_backend.domain;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class KamerFaseTest {

	@ParameterizedTest
	@EnumSource(KamerFase.class)
	void volgendeFase_geeftVolgendeFase(KamerFase kamerFase) {
		if (kamerFase.ordinal() == KamerFase.values().length - 1) {
			Assertions.assertThrows(NoSuchElementException.class, kamerFase::volgendeFase);
		} else {
			KamerFase volgendeFase = kamerFase.volgendeFase();
			Assertions.assertEquals(KamerFase.values()[kamerFase.ordinal() + 1], volgendeFase);
		}
	}

}
