package nl.han.oose.buizerd.projectcheck_backend.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RolTest {

	private static final String ROL_NAAM = "Opdrachtgever";
	private static final String BESCHRIJVING = "Lorem ipsum";

	private Rol sut;

	@BeforeEach
	void setUp() {
		sut = new Rol(ROL_NAAM, BESCHRIJVING);
	}

	@Test
	void getRolNaam_geeftJuisteWaarde() {
		// Act
		var actual = sut.getRolNaam();

		// Assert
		assertEquals(ROL_NAAM, actual);
	}

	@Test
	void getBeschrijving_geeftJuisteWaarde() {
		// Act
		var actual = sut.getBeschrijving();

		// Assert
		assertEquals(BESCHRIJVING, actual);
	}

}
