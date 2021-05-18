package nl.han.oose.buizerd.projectcheck_backend.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class RolConverterTest {

	private RolConverter rolConverter;
	private Rol rol;
	private static final String rolNaam = "OPDRACHTGEVER";

	@BeforeEach
	void setup() {
		rolConverter = Mockito.mock(RolConverter.class);
		rol = Rol.valueOf(rolNaam);
	}

	@Test
	void convertToDatabaseColumnTest() {
		// Arrange
		Mockito.when(rolConverter.convertToDatabaseColumn(rol)).thenReturn(rolNaam);

		var actual = rolConverter.convertToDatabaseColumn(rol);
		// Act & Assert
		Assertions.assertAll(
			() -> Mockito.verify(rolConverter).convertToDatabaseColumn(rol),
			() -> Assertions.assertEquals(rolNaam, actual)
		);
	}

	@Test
	void convertToEntityAttributeTest() {
		// Arrange
		Mockito.when(rolConverter.convertToEntityAttribute(rolNaam)).thenReturn(rol);

		var actual = rolConverter.convertToEntityAttribute(rolNaam);
		// Act & Assert
		Assertions.assertAll(
			() -> Mockito.verify(rolConverter).convertToEntityAttribute(rolNaam),
			() -> Assertions.assertEquals(rol, actual)
		);
	}

}
