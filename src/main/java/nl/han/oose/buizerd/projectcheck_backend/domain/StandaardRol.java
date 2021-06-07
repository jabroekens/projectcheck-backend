package nl.han.oose.buizerd.projectcheck_backend.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.executable.ValidateOnExecution;

/**
 * De standaard {@link Rol Rol(len)} van De ProjectCheck.
 */
public enum StandaardRol {

	DIRECTIE_MANAGEMENT(new Rol("Directie / Management")),
	OPDRACHTGEVER(new Rol("Opdrachtgever")),
	GEBRUIKER_EINDRESULTAAT(new Rol("Gebruiker van het eindresultaat")),
	PROJECTLEIDER(new Rol("Projectleider")),
	PROJECTTEAMLID(new Rol("Projectteamlid")),
	EXTERN_PROJECTTEAMLID(new Rol("Extern projectteamlid")),
	PROJECTOMGEVING(new Rol("Projectomgeving")),
	PROJECTBUREAU(new Rol("Projectbureau"));

	@NotNull
	private final Rol rol;

	@ValidateOnExecution
	StandaardRol(@NotNull Rol rol) {
		this.rol = rol;
	}

	public Rol getRol() {
		return rol;
	}

}
