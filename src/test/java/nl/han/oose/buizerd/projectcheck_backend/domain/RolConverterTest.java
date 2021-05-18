package nl.han.oose.buizerd.projectcheck_backend.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RolConverterTest {

	private RolConverter rolConverter;

	@BeforeEach
	void setUp() {
		rolConverter = new RolConverter();
	}

	@Test
	void convertToDatabaseColumnTest_rolAanwezig() {
		// Arange
		Rol rol = Rol.OPDRACHTGEVER;
		String rolNaam = "Opdrachtgever";
		// Act
		var actual = rolConverter.convertToDatabaseColumn(rol);
		// Assert
		Assertions.assertEquals(rolNaam, actual);
	}

	@Test
	void convertToDatabaseColumnTest_rolAfwezig() {
		// Arange
		Rol rol = null;
		String rolNaam = null;
		// Act
		var actual = rolConverter.convertToDatabaseColumn(rol);
		// Assert
		Assertions.assertEquals(rolNaam, actual);
	}

	@Test
	void convertToEntityAttributeTest_rolNaamAanwezig() {
		// Arange
		String rolNaam = "Opdrachtgever";
		Rol rol = Rol.OPDRACHTGEVER;
		// Act
		var actual = rolConverter.convertToEntityAttribute(rolNaam);
		// Assert
		Assertions.assertEquals(rol, actual);
	}

	@Test
	void convertToEntityAttributeTest_rolNaamAfwezig() {
		// Arange
		String rolNaam = null;
		Rol rol = null;
		// Act
		var actual = rolConverter.convertToEntityAttribute(rolNaam);
		// Assert
		Assertions.assertEquals(rol, actual);
	}

}
