package nl.han.oose.buizerd.projectcheck_backend.domain;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class Begeleider extends Deelnemer {

	public Begeleider() {
		// An empty constructor is required by JPA
	}

	public Begeleider(@NotNull DeelnemerId deelnemerId, @NotNull String naam) {
		super(deelnemerId, naam);
	}

}
