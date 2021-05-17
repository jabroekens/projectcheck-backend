package nl.han.oose.buizerd.projectcheck_backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.executable.ValidateOnExecution;
import nl.han.oose.buizerd.projectcheck_backend.Util;
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
	private Rol rol;


	/**
	 * De naam van de deelnemer.
	 */
	@Naam
	@Column(nullable = false)
	private String naam;

	/**
	 * De {@link Kamer} waaraan de deelnemer deelneemt.
	 */
	/*
	 * `@Util.GsonExclude` om StackOverflowError te voorkomen
	 * als Gson een Deelnemer serializeert (Deelnemer bevat
	 * Kamer en Kamer bevat Deelnemer).
	 *
	 * Als we het veld als `transient` markeren wordt het ook
	 * niet gedeserializeerd door JPA, wat wel moet.
	 */
	@Util.GsonExclude
	@ManyToOne(optional = false)
	@MapsId("kamerCode")
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
	 * @throws IllegalStateException Als de kamer waaraan de deelnemer deelneemt {@code null} is.
	 * @see Kamer#voegDeelnemerToe(Deelnemer)
	 */
	public Kamer getKamer() {
		if (kamer == null) {
			throw new IllegalStateException("De deelnemer neemt nog niet deel aan een kamer.");
		}

		return kamer;
	}

	/**
	 * Zet de {@link Kamer} waaraan de deelnemer deelneemt.
	 * <p>
	 * <em>Deze methode wordt gebruikt door {@link Kamer#voegDeelnemerToe(Deelnemer)}
	 * en mag hierbuiten niet aangeroepen worden.</em>
	 * <p>
	 * Om een kip-en-eiprobleem te voorkomen is het nodig dat de kamer achteraf
	 * bij de {@link Deelnemer} gezet wordt. Een deelnemer is namelijk altijd
	 * gekoppeld aan een {@link Kamer}, en een kamer heeft altijd een
	 * {@link Begeleider}.
	 */
	void setKamer(Kamer kamer) {
		this.kamer = kamer;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

}
