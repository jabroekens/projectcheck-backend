package nl.han.oose.buizerd.projectcheck_backend.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class StandaardRolTest {

	@ParameterizedTest
	@EnumSource(StandaardRol.class)
	void getRol_isNietNull(StandaardRol standaardRol) {
		Assertions.assertNotNull(standaardRol.getRol());
	}

}
