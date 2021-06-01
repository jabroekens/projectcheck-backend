package nl.han.oose.buizerd.projectcheck_backend.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RolTest {

	private static final String ROL_NAAM = "Opdrachtgever";

	private Rol sut;

	@BeforeEach
	void setUp() {
		sut = new Rol(RolTest.ROL_NAAM);
	}

	@Test
	void getRolNaam_geeftJuisteWaarde() {
		// Act
		var actual = sut.getRolNaam();

		// Assert
		assertEquals(RolTest.ROL_NAAM, actual);
	}

}
