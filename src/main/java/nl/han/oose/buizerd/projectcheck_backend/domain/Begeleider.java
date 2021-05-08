package nl.han.oose.buizerd.projectcheck_backend.domain;

import javax.persistence.Entity;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.executable.ValidateOnExecution;
import nl.han.oose.buizerd.projectcheck_backend.validation.constraints.Naam;

/**
 * Een begeleider is iemand die een {@link Kamer} begeleidt.
 */
@Entity
public class Begeleider extends Deelnemer {

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

}
