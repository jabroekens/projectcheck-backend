package nl.han.oose.buizerd.projectcheck_backend.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.executable.ValidateOnExecution;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import nl.han.oose.buizerd.projectcheck_backend.ExcludeFromGeneratedCoverageReport;
import nl.han.oose.buizerd.projectcheck_backend.validation.constraints.KamerCode;

/**
 * Een kamer is een uitvoering van De ProjectCheck waar deelnemers zich
 * aan bij kunnen sluiten doormiddel van een unieke code.
 * <p>
 * Elke kamer wordt begeleid door een begeleider en heeft een aantal
 * relevante rollen die vertegenwoordigd kunnen worden door de
 * deelnemers.
 */
@Entity
public class Kamer {

	public static final int KAMER_CODE_MAX_LENGTE = 6;

	@KamerCode
	@Id
	@Column(nullable = false, updatable = false)
	private String kamerCode;

	@NotNull
	@PastOrPresent
	@Column(nullable = false, updatable = false)
	private LocalDateTime datum;

	@NotNull
	@Valid
	@Column(nullable = false)
	private KamerFase kamerFase;

	@Transient
	private Ronde huidigeRonde;

	@ManyToMany(fetch = FetchType.EAGER)
	private Set<@NotNull @Valid Rol> relevanteRollen;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "kamer", orphanRemoval = true)
	private Set<@NotNull @Valid Deelnemer> deelnemers;

	/**
	 * @deprecated wordt gebruikt door JPA en mag niet aangeroepen worden
	 */
	@ExcludeFromGeneratedCoverageReport(reason = "wordt gebruikt door JPA en mag niet aangeroepen worden")
	@Deprecated
	protected Kamer() {
	}

	/**
	 * Construeert een {@link Kamer} in de fase {@link KamerFase#SETUP}.
	 */
	@ValidateOnExecution
	public Kamer(@KamerCode String kamerCode, @NotNull @Valid Begeleider begeleider) {
		this.kamerCode = kamerCode;

		datum = LocalDateTime.now();
		kamerFase = KamerFase.SETUP;
		relevanteRollen = new HashSet<>();
		deelnemers = new HashSet<>();

		addDeelnemer(begeleider);
	}

	public String getKamerCode() {
		return kamerCode;
	}

	public LocalDateTime getDatum() {
		return datum;
	}

	public KamerFase getKamerFase() {
		return kamerFase;
	}

	@ValidateOnExecution
	public void setKamerFase(@NotNull @Valid KamerFase kamerFase) {
		this.kamerFase = kamerFase;
	}

	@ExcludeFromGeneratedCoverageReport(reason = "without using reflection, 'begeleider' can never be null")
	public Begeleider getBegeleider() {
		var begeleider = deelnemers.stream()
		                           .filter(Begeleider.class::isInstance)
		                           .map(Begeleider.class::cast)
		                           .findAny();

		if (begeleider.isEmpty()) {
			/*
			 * Gooi een IllegalStateException, omdat het niet voor hoort
			 * te komen dat er geen begeleider aanwezig is. Deze wordt
			 * namelijk normaliter in de constructor toegevoegd aan
			 * `deelnemers`.
			 */
			throw new IllegalStateException("De kamer heeft geen begeleider.");
		}

		return begeleider.get();
	}

	/**
	 * Haal een read-only kopie van de deelnemers van de kamer op.
	 *
	 * @see Collections#unmodifiableSet(Set)
	 */
	public Set<Deelnemer> getDeelnemers() {
		return Collections.unmodifiableSet(deelnemers);
	}

	@ValidateOnExecution
	public Optional<Deelnemer> getDeelnemer(@NotNull @Valid DeelnemerId deelnemerId) {
		return deelnemers.stream().filter(d -> d.getDeelnemerId().equals(deelnemerId)).findAny();
	}

	/**
	 * Voegt een deelnemer toe aan de kamer en zet de kamer van de deelnemer.
	 * <p>
	 * Om een kip-en-eiprobleem te voorkomen is het nodig dat de kamer <em>achteraf</em>
	 * bij de {@link Deelnemer} gezet wordt. Zowel {@link Deelnemer} als {@link Kamer}
	 * zijn namelijk niet geldig zonder elkaar.
	 *
	 * @see Deelnemer#setKamer(Kamer)
	 */
	@ValidateOnExecution
	public void addDeelnemer(@NotNull @Valid Deelnemer deelnemer) {
		deelnemers.add(deelnemer);
		deelnemer.setKamer(this);
	}

	/**
	 * Genereert een ID dat gebruikt wordt in {@link DeelnemerId}.
	 */
	public Long genereerDeelnemerId() {
		return deelnemers.size() + 1L;
	}

	/**
	 * Haal een read-only kopie van de relevante rollen van de kamer op.
	 *
	 * @see Collections#unmodifiableSet(Set)
	 */
	@ValidateOnExecution
	public Set<Rol> getRelevanteRollen() {
		return Collections.unmodifiableSet(relevanteRollen);
	}

	@ValidateOnExecution
	public void setRelevanteRollen(@NotNull @Valid Set<Rol> rollen) {
		relevanteRollen = rollen;
	}

	public Optional<Ronde> getHuidigeRonde() {
		return Optional.ofNullable(huidigeRonde);
	}

}
