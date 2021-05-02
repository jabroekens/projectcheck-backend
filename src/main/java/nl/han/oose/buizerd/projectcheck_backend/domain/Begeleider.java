package nl.han.oose.buizerd.projectcheck_backend.domain;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

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
	protected Begeleider() {
	}

	/**
	 * Construeert een {@link Begeleider}.
	 *
	 * @param kamer De {@link Kamer} die de begeleider begeleidt.
	 * @param naam De naam van de begeleider.
	 * @see nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer
	 */
	public Begeleider(@NotNull Kamer kamer, @NotNull String naam) {
		super(kamer, naam);
	}

}
