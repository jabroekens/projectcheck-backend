package nl.han.oose.buizerd.projectcheck_backend.domain;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class StandaardRolTest {

	@ParameterizedTest
	@EnumSource(StandaardRol.class)
	void getRol_isNietNull(StandaardRol standaardRol) {
		assertNotNull(standaardRol.getRol());
	}

}
