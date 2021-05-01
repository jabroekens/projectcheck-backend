package nl.han.oose.buizerd.projectcheck_backend.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.validation.constraints.NotNull;

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
	@ManyToOne(cascade = CascadeType.ALL)
	private Kamer kamer;

	/**
	 * De naam van de deelnemer.
	 */
	@Column(nullable = false)
	private String naam;

	public Deelnemer() {
		// Een lege constructor is vereist door JPA.
	}

	public Deelnemer(@NotNull Kamer kamer, @NotNull String naam) {
		this.deelnemerId = new DeelnemerId(kamer);
		this.naam = naam;
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

	/**
	 * Zet de naam van de deelnemer.
	 *
	 * @param naam De nieuwe naam van de deelnemer.
	 */
	public void setNaam(@NotNull String naam) {
		this.naam = naam;
	}

}
