package nl.han.oose.buizerd.projectcheck_backend.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class RolConverter implements AttributeConverter<Rol, String> {

	@Override
	public String convertToDatabaseColumn(Rol attribute) {
		return attribute == null ? null : attribute.getRolNaam();
	}

	@Override
	public Rol convertToEntityAttribute(String dbData) {
		if (dbData == null) {
			return null;
		}

		return Stream.of(Rol.values())
					 .filter(r -> r.getRolNaam().equals(dbData))
					 .findFirst()
					 .orElseThrow(IllegalArgumentException::new);
	}

}
