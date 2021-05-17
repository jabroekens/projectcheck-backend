package nl.han.oose.buizerd.projectcheck_backend.domain;

/**
 * Een rol is een set van rechten die een {@link Deelnemer} aangewezen kan krijgen of kan kiezen binnen een {@link Kamer}.
 * <p>
 * Elke rol heeft een naam.
 */
public enum Rol {
	EINDGEBRUIKER("Eindgebruiker"),
	PROJECTLEIDER("Projectleider"),
	OPDRACHTGEVER("Opdrachtgever"),
	TEAMLID("Teamlid"),
	EXTERNE_PROJECTBEGELEIDER("Externe Projectbegeleider"),
	EXTERNE_OPDRACHTGEVER("Externe Opdrachtgever"),
	PROJECTBUREAU("Projectbureau");

	/**
	 * De naam van de rol.
	 */
	private final String rolNaam;


	/**
	* Construeert een {@link Rol}.
	*
	* @param rolNaam De naam van de rol.
	*/
	Rol(String rolNaam) {
		this.rolNaam = rolNaam;
	}

	/**
	 * Haal de naam van de rol op.
	 *
	 * @return De naam van de rol.
	 */
	public String getRolNaam() {
		return rolNaam;
	}
}
