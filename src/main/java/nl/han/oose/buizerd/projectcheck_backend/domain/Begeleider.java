package nl.han.oose.buizerd.projectcheck_backend.domain;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class Begeleider extends Deelnemer {

	public Begeleider() {
		// Een lege constructor is vereist door JPA.
	}

	public Begeleider(@NotNull DeelnemerId deelnemerId, @NotNull String naam) {
		super(deelnemerId, naam);
	}

}
