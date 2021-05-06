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
 * <p>
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

	/**
	 * Construeert een {@link Deelnemer}.
	 * <p>
	 * <b>Deze constructor wordt gebruikt door JPA en mag niet aangeroepen worden.</b>
	 */
	public Deelnemer() {
	}

	/**
	 * Construeert een {@link Deelnemer}.
	 *
	 * @param deelnemerId De {@link DeelnemerId} die de deelnemer identificeert.
	 * @param naam De naam van de deelnemer.
	 */
	public Deelnemer(@NotNull DeelnemerId deelnemerId, @NotNull String naam) {
		this.deelnemerId = deelnemerId;
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
