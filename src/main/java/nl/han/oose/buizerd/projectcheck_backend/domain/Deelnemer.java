package nl.han.oose.buizerd.projectcheck_backend.domain;

import com.google.gson.annotations.Expose;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.executable.ValidateOnExecution;
import nl.han.oose.buizerd.projectcheck_backend.ExcludeFromGeneratedCoverageReport;
import nl.han.oose.buizerd.projectcheck_backend.validation.constraints.Naam;

/**
 * Een deelnemer is iemand die deelneemt aan een {@link Kamer}.
 * <p>
 * Elke deelnemer heeft een naam.
 */
@Entity
public class Deelnemer {

	@Expose
	@Valid
	@ManyToOne(cascade = CascadeType.ALL)
	private KaartenSelectie kaartenSelectie;

	/**
	 * De identifier van de deelnemer.
	 */
	/*
	 * Om te voorkomen dat `kamer` gedeserializeert wordt door Gson en een StackOverfowError
	 * veroorzaakt als gevolg van zelfreferentie (een deelnemer bevat een kamer, en een kamer
	 * bevat een deelnemer), moeten we alle velden *behalve* `kamer` markeren met `@Expose`.
	 * Helaas is er geen `@Exclude` standaard inbegrepen bij Gson.
	 *
	 * Zie: https://github.com/google/gson/pull/1262
	 */
	@Expose
	@NotNull
	@Valid
	@EmbeddedId
	private DeelnemerId deelnemerId;

	@Expose
	@ManyToOne
	private Rol rol;

	@Expose
	@Naam
	@Column(nullable = false)
	private String naam;

	@ManyToOne(optional = false)
	@MapsId("kamerCode")
	private Kamer kamer;

	/**
	 * @deprecated wordt gebruikt door JPA en mag niet aangeroepen worden
	 */
	@ExcludeFromGeneratedCoverageReport(reason = "wordt gebruikt door JPA en mag niet aangeroepen worden")
	@Deprecated
	protected Deelnemer() {
	}

	@ValidateOnExecution
	public Deelnemer(@NotNull @Valid DeelnemerId deelnemerId, @Naam String naam) {
		this.deelnemerId = deelnemerId;
		this.naam = naam;
	}

	public DeelnemerId getDeelnemerId() {
		return deelnemerId;
	}

	public String getNaam() {
		return naam;
	}

	@ValidateOnExecution
	public void setNaam(@Naam String naam) {
		this.naam = naam;
	}

	/**
	 * @see Kamer#addDeelnemer(Deelnemer)
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
	 * <em>Deze methode wordt gebruikt door {@link Kamer#addDeelnemer(Deelnemer)}
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

	@ValidateOnExecution
	public void setRol(@NotNull @Valid Rol rol) {
		this.rol = rol;
	}

	public KaartenSelectie getKaartenSelectie() {
		return kaartenSelectie;
	}

	public void setKaartenSelectie(@NotNull @Valid KaartenSelectie selectie) {
		this.kaartenSelectie = selectie;
	}
}
