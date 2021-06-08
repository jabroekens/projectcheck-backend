package nl.han.oose.buizerd.projectcheck_backend.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class StandaardKaartenSetTest {

	@ParameterizedTest
	@EnumSource(StandaardKaartenSet.class)
	void kaartenSetIsNotNull(StandaardKaartenSet standaardKaartenSet) {
		Assertions.assertNotNull(standaardKaartenSet.getKaartenSet());
	}

}
