package nl.han.oose.buizerd.projectcheck_backend.domain;

public enum Rol {
	EINDGEBRUIKER("Eindgebruiker"),
	PROJECTLEIDER("Projectleider"),
	OPDRACHTGEVER("Opdrachtgever"),
	TEAMLID("Teamlid"),
	EXTERNE_PROJECTBEGELEIDER("Externe Projectbegeleider"),
	EXTERNE_OPDRACHTGEVER("Externe Opdrachtgever"),
	PROJECTBUREAU("Projectbureau");

	private final String rolNaam;

	Rol(String rolNaam) {
		this.rolNaam = rolNaam;
	}

	public String getRolNaam() {
		return rolNaam;
	}
}
