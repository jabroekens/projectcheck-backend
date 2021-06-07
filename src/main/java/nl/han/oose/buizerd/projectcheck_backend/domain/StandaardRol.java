package nl.han.oose.buizerd.projectcheck_backend.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.executable.ValidateOnExecution;

/**
 * De standaard {@link Rol Rol(len)} van De ProjectCheck.
 */
public enum StandaardRol {

	DIRECTIE_MANAGEMENT(new Rol("Directie / Management", "Stelt vast of het project bijdraagt aan de strategie en doelstellingen van de organisatie.")),
	OPDRACHTGEVER(new Rol("Opdrachtgever", "Verantwoordelijk voor de lange termijndoelen van het project en het leveren van de benodigde middelen.")),
	GEBRUIKER_EINDRESULTAAT(new Rol("Gebruiker van het eindresultaat", "Wil dat het resultaat voorziet in hun behoeften.")),
	PROJECTLEIDER(new Rol("Projectleider", "Verantwoordelijk voor het naar tevredenheid opleveren van het projectresultaat.")),
	PROJECTTEAMLID(new Rol("Projectteamlid", "Draagt bij aan de realisatie van het projectresultaat vanuit hun expertisegebied.")),
	EXTERN_PROJECTTEAMLID(new Rol("Extern projectteamlid", "Draagt bij vanuit hun expertisegebied én aan de doelstellingen van de eigen organisatie.")),
	PROJECTOMGEVING(new Rol("Projectomgeving", "Ondervindt effecten, maar heeft niet direct baat bij het project.")),
	PROJECTBUREAU(new Rol("Projectbureau", "Dienstverlenend aan het project en met name de projectleider."));

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
