package nl.han.oose.buizerd.projectcheck_backend.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class RolTest {

	private static final String rolNaam = "OPDRACHTGEVER";
	private Rol rol;

	@BeforeEach
	void setUp() {
		rol = Mockito.mock(Rol.class);
	}

	@Test
	void getRolnaamTest() {
		// Arrange
		Mockito.when(rol.getRolNaam()).thenReturn(rolNaam);

		// Act
		var actual = rol.getRolNaam();

		// Assert
		Assertions.assertAll(
			() -> Mockito.verify(rol).getRolNaam(),
			() -> Assertions.assertEquals(rolNaam, actual)
		);
	}

}
