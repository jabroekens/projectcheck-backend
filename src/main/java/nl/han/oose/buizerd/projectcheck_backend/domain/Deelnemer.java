package nl.han.oose.buizerd.projectcheck_backend.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.validation.constraints.NotNull;
import nl.han.oose.buizerd.projectcheck_backend.exceptions.InvalideGebruikerException;

/**
 * Een deelnemer is iemand die deelneemt aan een {@link Kamer}.
 * Elke deelnemer heeft een naam.
 */
@Entity
public class Deelnemer {

	/**
	 * De identifier van de deelnemer.
	 */
	@EmbeddedId
	private DeelnemerId deelnemerId;

	/**
	 * De {@link Kamer} waaraan de deelnemer deelneemt.
	 */
	@MapsId("kamerCode")
	@ManyToOne(fetch = FetchType.LAZY)
	private Kamer kamer;

	/**
	 * De naam van de deelnemer.
	 */
	@Column(nullable = false, updatable = false)
	private String naam;

	public Deelnemer() {
		// Een lege constructor is vereist door JPA.
	}

	// Een constructor voor tests.
	Deelnemer(@NotNull DeelnemerId deelnemerId, @NotNull String naam) {
		this.deelnemerId = deelnemerId;
		this.naam = valideerGebruikersnaam(naam);
	}

	private String valideerGebruikersnaam(String naam) throws InvalideGebruikerException {
		// TODO @Luka: uitwerken in code
	}

	/**
	 * Haal de identifier van de deelnemer op.
	 *
	 * @return De identifier van de deelnemer.
	 */
	public DeelnemerId getDeelnemerId() {
		return deelnemerId;
	}

	/**
	 * Haal de naam van de deelnemer op.
	 *
	 * @return De naam van de deelnemer.
	 */
	public String getNaam() {
		return naam;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Deelnemer deelnemer = (Deelnemer) o;
		return deelnemerId.equals(deelnemer.deelnemerId) && kamer.equals(deelnemer.kamer) && naam.equals(deelnemer.naam);
	}

	@Override
	public int hashCode() {
		return Objects.hash(deelnemerId, kamer, naam);
	}

}
