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
		assertEquals(ROL_NAAM, sut.getRolNaam());
	}

	@Test
	void getBeschrijving_geeftJuisteWaarde() {
		assertEquals(BESCHRIJVING, sut.getBeschrijving());
	}

}
