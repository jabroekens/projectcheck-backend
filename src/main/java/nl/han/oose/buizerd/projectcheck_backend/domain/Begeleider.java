package nl.han.oose.buizerd.projectcheck_backend.domain;

import jakarta.persistence.Entity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.executable.ValidateOnExecution;
import nl.han.oose.buizerd.projectcheck_backend.ExcludeFromGeneratedCoverageReport;
import nl.han.oose.buizerd.projectcheck_backend.validation.constraints.Naam;

/**
 * Een begeleider is een deelnemer die een {@link Kamer} begeleidt.
 */
@Entity
public class Begeleider extends Deelnemer {

	/**
	 * @deprecated wordt gebruikt door JPA en mag niet aangeroepen worden
	 */
	@ExcludeFromGeneratedCoverageReport(reason = "wordt gebruikt door JPA en mag niet aangeroepen worden")
	@Deprecated
	protected Begeleider() {
	}

	@ValidateOnExecution
	public Begeleider(@NotNull @Valid DeelnemerId deelnemerId, @Naam String naam) {
		super(deelnemerId, naam);
	}

}
