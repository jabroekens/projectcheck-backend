package nl.han.oose.buizerd.projectcheck_backend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.executable.ValidateOnExecution;

/**
 * Een rol is een set van rechten die een {@link Deelnemer} aangewezen kan krijgen of kan kiezen binnen een {@link Kamer}.
 * <p>
 * Elke rol heeft een naam.
 */
@Entity
public class Rol {

	/**
	 * De naam van de rol.
	 */
	@NotNull
	@Id
	private String rolNaam;

	/**
	 * Construeert een {@link Rol}.
	 * <p>
	 * <b>Deze constructor wordt gebruikt door JPA en mag niet aangeroepen worden.</b>
	 */
	public Rol() {
	}

	/**
	 * Construeert een {@link Rol}.
	 *
	 * @param rolNaam De naam van de rol.
	 */
	@ValidateOnExecution
	public Rol(@NotNull String rolNaam) {
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
