package nl.han.oose.buizerd.projectcheck_backend.domain;

public enum StandaardRol {

	EXTERN_PROJECTTEAMLID(new Rol("Extern Projectteamlid")),
	GEBRUIKER_PROJECTRESULTAAT(new Rol("Gebruiker Projectresultaat")),
	DIRECTIE_MANAGEMENT(new Rol("Directie Management")),
	PROJECT_TEAM_MEMBER(new Rol("Project Team Member")),
	PROJECTLEIDER(new Rol("Projectleider")),
	PROJECTTEAMLID(new Rol("Projectteamlid")),
	OPDRACHTGEVER(new Rol("Opdrachtgever"));

	private final Rol rol;

	StandaardRol(Rol rol) {
		this.rol = rol;
	}

	public Rol getRol() {
		return this.rol;
	}

}
