package nl.han.oose.buizerd.projectcheck_backend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.executable.ValidateOnExecution;
import nl.han.oose.buizerd.projectcheck_backend.validation.constraints.Naam;

/**
 * Een begeleider is iemand die een {@link Kamer} begeleidt.
 */
@Entity
public class Begeleider extends Deelnemer {

	@Transient
	@Valid
	private Kamer kamer;

	/**
	 * Construeert een {@link Begeleider}.
	 * <p>
	 * <b>Deze constructor wordt gebruikt door JPA en mag niet aangeroepen worden.</b>
	 */
	public Begeleider() {
	}

	/**
	 * Construeert een {@link Begeleider}.
	 *
	 * @param deelnemerId De {@link DeelnemerId} die de begeleider identificeerd.
	 * @param naam De naam van de begeleider.
	 * @see nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer
	 */
	@ValidateOnExecution
	public Begeleider(@NotNull @Valid DeelnemerId deelnemerId, @Naam String naam) {
		super(deelnemerId, naam);
	}

	/**
	 * Roept de methode binnen de Kamer class in die een rol inschakelt voor die kamer.
	 *
	 * @see nl.han.oose.buizerd.projectcheck_backend.domain.Kamer
	 */
	@ValidateOnExecution
	public void activeerRol(String rol) {
		kamer.activeerRelevanteRol(rol);
	}

	public void setKamer(Kamer kamer) {
		this.kamer = kamer;
	}

}
