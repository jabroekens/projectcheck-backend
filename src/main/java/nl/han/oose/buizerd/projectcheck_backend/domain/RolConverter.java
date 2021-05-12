package nl.han.oose.buizerd.projectcheck_backend.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RolConverter implements AttributeConverter<Rol, String> {

	@Override
	public String convertToDatabaseColumn(Rol attribute) {
		return attribute == null ? null : attribute.getRolNaam();
	}

	@Override
	public Rol convertToEntityAttribute(String dbData) {
		return dbData == null ? null : Rol.valueOf(dbData);
	}

}
