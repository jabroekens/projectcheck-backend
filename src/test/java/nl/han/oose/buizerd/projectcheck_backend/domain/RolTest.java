package nl.han.oose.buizerd.projectcheck_backend.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RolTest {

	private static final String ROL_NAAM = "OPDRACHTGEVER";

	private Rol rol;

	@BeforeEach
	void setUp() {
		rol = new Rol(RolTest.ROL_NAAM);
	}

	@Test
	void getRolnaamTest() {
		// Act
		var actual = rol.getRolNaam();

		// Assert
		Assertions.assertEquals(RolTest.ROL_NAAM, actual);
	}

}
