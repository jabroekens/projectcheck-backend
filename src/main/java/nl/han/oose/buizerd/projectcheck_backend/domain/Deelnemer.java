package nl.han.oose.buizerd.projectcheck_backend.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.executable.ValidateOnExecution;
import nl.han.oose.buizerd.projectcheck_backend.validation.constraints.Naam;

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
	@NotNull
	@Valid
	@EmbeddedId
	private DeelnemerId deelnemerId;

	/**
	 * De naam van de deelnemer.
	 */
	@Naam
	@Column(nullable = false)
	private String naam;

	/**
	 * De {@link Kamer} waaraan de deelnemer deelneemt.
	 */
	@MapsId("kamerCode")
	@ManyToOne(cascade = CascadeType.ALL)
	private Kamer kamer;

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
	@ValidateOnExecution
	public Deelnemer(@NotNull @Valid DeelnemerId deelnemerId, @Naam String naam) {
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
	@ValidateOnExecution
	public void setNaam(@Naam String naam) {
		this.naam = naam;
	}

	/**
	 * Haal de kamer waaraan de deelnemer deelneemt op.
	 *
	 * @return De kamer waaraan de deelnemer deelneemt.
	 */
	public Kamer getKamer() {
		return kamer;
	}

}
